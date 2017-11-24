package EhNew;

import EhNew.math.Matrix4f;
import EhNew.math.ProjectionTransform;
import EhNew.util.TickManager;
import EhNew.util.Camera;
import EhNew.math.Vec2;
import EhNew.shaders.FactoryShader;
import EhNew.shaders.Shader;
import EhNew.util.TextFactory;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * @since 20 Jan, 2016
 * @author Abhishek
 */
public class Engine {
    private int fps;
    private long window;
    protected Camera c;
    protected ProjectionTransform t;
    private Level current;
    private TickManager updater, renderer;
    protected TextFactory text;
    protected Shader currshader;
    protected int defIdentityInstTranformTextureID;
    
    
    /**
     * Standard Constructor, makes no OpenGL calls therefore doesn't need a hardware 
     * accelerated context.
     */
    public Engine(){
        c = new Camera();
        t = new ProjectionTransform();
        window = -1;
        current = null;
        updater = null;
        renderer = null;
        currshader = new FactoryShader();
        text = new TextFactory();
    }
    
    /**
     * This function initializes the GLFW window and an OpenGL compatibility 
     * context. Version checking is not currently supported. It associated the 
     * created GLContext to the Renderer thread, making it the only thread available to 
     * call OpenGL functions.
     * @param title The WIndow Title
     * @param width Width of the Window in pixels
     * @param height Height of the window in pixels
     * @param f Ideal value of the Frames Per Second for this window
     */
    public void init(String title, int width, int height, int f) {
        t.width = width;
        t.aspectRatio = (float)width /(float) height;
        this.fps = f;
        System.out.println("ENGINE: Initiating...");
        try {
            renderer = new TickManager(() -> {
                if (glfwInit() != true) {
                    throw new IllegalStateException("Failed to init glfw");
                }
                glfwDefaultWindowHints();

                window = glfwCreateWindow(width, height, title, 0, 0);
                if (window == 0) {
                    throw new IllegalStateException("Failed to init window");
                }
                glfwMakeContextCurrent(window);
                glfwSwapInterval(0);
                glfwShowWindow(window);
                GL.createCapabilities();
                glFrontFace(GL_CW);
                glCullFace(GL_BACK);
                glEnable(GL_CULL_FACE);
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                glEnable(GL_DEPTH_TEST);
                glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
                glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
                glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
                glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
                currshader.init();
                currshader.loadShader();
                
                defIdentityInstTranformTextureID = glGenTextures();
                glEnable(GL_TEXTURE_1D);
                GL13.glActiveTexture(currshader.getInstanceTransformMapTextureUnit());
                glBindTexture(GL_TEXTURE_1D, defIdentityInstTranformTextureID);
                GL20.glUniform1i(currshader.getUniformLocation(((FactoryShader)currshader).UNIFORM_INSTANCE_TRANSFORM_MAP_SIZE), 4);
                FloatBuffer g = BufferUtils.createFloatBuffer(16);
                g.put(new float[]{
//                    (byte)0xff, (byte)0x00, (byte)0x00, (byte)0x00,   // 0  1  2  3
//                    (byte)0x00, (byte)0xff, (byte)0x00, (byte)0x00,   // 4  5  6  7
//                    (byte)0x00, (byte)0x00, (byte)0xff, (byte)0x00,   // 8  9 10 11
//                    (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xff,});//12 13 14 15
                    1, 0, 0, 0,
                    0, 1, 0, 0,
                    0, 0, 1, 0,
                    0, 0, 0, 1});
                g.flip();
                glTexImage1D(GL_TEXTURE_1D, 0, GL30.GL_RGBA32F, 4, 0, GL_RGBA, GL_FLOAT, g);
                glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
                
                glDisable(GL_TEXTURE_1D);
            }, "Renderer");
            renderer.setTickDelay(1000/fps);
            renderer.runOnce();
            renderer.waitTillThreadPauses();
            System.out.println("ENGINE: Engine successfully Initiated");
        } catch(Exception e) {
            glfwTerminate();
        }
    }
    
    /**
     * Starts the renderer and updater threads to draw() and update() functions of 
     * the given Level.
     * @param l Level to be drawn and updated.
     */
    final public void start(Level l){
        if(current == null){
            current = l;
        }
        if(updater == null){
            updater = new TickManager(current::update, "Updater");
        }
        
        renderer.setCallback(this::gameLoop);
        
        System.out.println("ENGINE: Renderer Started");
        renderer.resume();
        updater.start();
    }
    
    /**
     * Stops the Renderer and The Updater Threads. SInce the Engine doesn't control
     * level specific OpenGL hardware resources that may need to be released,
     * it calls that destroy() function of the Current Level.
     */
    final public void stop(){
        renderer.stop();
        if(current != null) current.destroy();
        if(updater != null) updater.stop();
        glfwDestroyWindow(window);
        glfwTerminate();
    }
    
    /**
     * Calls the draw() function of the given entity exactly once, and thereafter 
     * resuming the normal gameLoop() functionality. 
     * @param e Entity to be drawn Once.
     */
    final public void drawOnce(Entity e){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        draw(e);
        glfwSwapBuffers(window);
        glfwPollEvents();
    }
    /**
     * Really just saves the Client from some effort.
     */
    final public void clearScreen(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    /**
     * Convenience function.
     */
    final public void updateScreen(){
        glfwSwapBuffers(window);
    }
    
    final public void updateFrameSize(){
        IntBuffer w = BufferUtils.createIntBuffer(1), h = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(window, w, h);
        t.width = w.get(0);
        t.aspectRatio = t.width / h.get(0);
        glViewport(0,0,w.get(0),h.get(0));
        currshader.updateProjection(t.calculateProjection());
        currshader.updateTransformation(t.calculateTransformation());
    }
    
    /**
     * Standard gameLoop(). It doesn't contain the loop itself, which is handled by 
     * the TickManager class. It simply states the contents of the gameLoop(), allowing
     * future subclasses of this Engine easy access to high level instructions only.
     */
    protected void gameLoop() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        current.draw();
        glfwSwapBuffers(window);
        glfwPollEvents();
        if (glfwWindowShouldClose(window) != false) {
            stop();
        }
    }
    
    /**
     * Convenience function.
     */
    public void pauseRenderer(){
        System.out.println("ENGINE: Pausing Renderer");
        if(Thread.currentThread().getName().equals("Renderer")){
            System.out.println("ENGINE: Renderer Thread Calls for pausing itself, Puase request ignored.");
            updater.pause();
        }else{
            renderer.pause();
            updater.pause();
        }
    }
    /**
     * Convenience Function
     */
    public void resumeRenderer(){
        System.out.println("ENGINE: Renderer Resumed");
        renderer.setCallback(this::gameLoop);
        renderer.resume();
        updater.resume();
    }
    
    /**
     * Uses the TickManager's RunOnce() functionality, allowing OpenGL calls to be
     * initiated by a Thread that couldn't normally do that. Drawing and buffering are 
     * done on the same Thread and only one thread can hold and OpenGL context,
     * this allows loading to be done while drawing is paused. Note that it doesn't
     * the level instantaneously. It waits for the next iteration before it starts
     * loading.
     * @param l Level to be Loaded.
     */
    public void loadLevel(Level l){
        if(l == null) return;
        if(renderer.getState() == TickManager.STATE_RUNNING) pauseRenderer();
        if(Thread.currentThread().getName().equals("Renderer")){
            System.out.println("ENGINE: Renderer Thread called upon itself to loadLevel, loading Level");
            l.load();
            return;
        }
        renderer.setCallback(l::load);
        renderer.runOnce();
        
        renderer.waitTillThreadPauses();
    }
    /**
     * Similar to loadLevel() function in terms of how it gets the work done,
     * However it gets the opposite work done.
     */
    public void unLoadLevel(){
        if(current == null) return;
        
        if(Thread.currentThread().getName().equals("Renderer")){
            System.out.println("ENGINE: Renderer Thread called upon itself to unloadLevel, unloading Level");
            current.destroy();
            return;
        }
        
        if(renderer.getState() == TickManager.STATE_RUNNING) pauseRenderer();
        
        renderer.setCallback(current::destroy);
        renderer.runOnce();
        
        renderer.waitTillThreadPauses();
    }
    
    /**
     * The given Level is made the current Level for all future draw calls and update 
     * callbacks. This is the level is that going to be played unless it's a 
     * utility level that streams multiple smaller levels within itself.
     * @param l Level to be drawn and updated after this function has returned
     */
    public void registerLevel(Level l){
        if(updater == null){
            updater = new TickManager(l::update, "Updater");
        }
        else{
            updater.setCallback(l::update);
        }
        current = l;
    }
    /**
     * For multiple shading to work correctly, this function must be called with
     * and appropriate shader before anything is done including but not limited to
     * Uniform querying, drawing, Uniform setting.
     * @param f The shader to be used for all OpenGL calls
     */
    public void switchToShader(Shader f){
        //For shaders to work correctly, you must use this function to switch to them before doing 
        //anything, or commands get mixed up among the shaders programs
        currshader = f;
        f.useShader();
    }
    
    public void switchToLevel(Level l){
        System.out.println("Switching to New Level...");
        pauseRenderer();
        unLoadLevel();
        loadLevel(l);
        registerLevel(l);
        resumeRenderer();
    }
    
    public Shader getShader(){
        return currshader;
    }
    public TextFactory getTextFactory(){
        return text;
    }
    public Camera getCamera(){
        return c;
    }
    public long getWindow(){
        return window;
    }
    public ProjectionTransform getProjeTransform(){
        return t;
    }
    public Vec2 transformGLFWCoodsToOpenGLCoods(float x, float y){
        float height = t.width/t.aspectRatio;
        x = (x - t.width/2)/t.width;
        y = -(y - height/2)/height;
        return new Vec2(x, y);
    }
    public Vec2 getMousePosinOpenGLCoods(){
        DoubleBuffer mouseX = BufferUtils.createDoubleBuffer(1), mouseY = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(window, mouseX, mouseY);
        float height = t.width/t.aspectRatio;
        return new Vec2(
            ((float)mouseX.get(0) - t.width/2)*2/t.width,
            (height/2 - (float)mouseY.get(0))*2/height);
    }
    
    //TODO
    public int getDefaultIdentityInstanceTransformTexture(){
        System.out.println("TODO: getDefaultIdentityInstanceTransformTexture()");
        return 0;
    }
    
    /**
     * Assuming basic shader functionality to be available in the current Shader, 
     * this function updates the standard Transformation Matrices, also called the 
     * Model Matrices. It then makes uniform calls on the shader to update the Matrix
     * and draws the given Entity with the current DrawMode.
     * @param e 
     */
    public void draw(Entity e){
        t.translate(e.translation);
        t.rotate(e.rotation);
        t.scale(e.scale);
        currshader.updateTransformation(t.calculateTransformation());
        e.draw();
    }
    
    /**
     * Check the draw(Entity e) function.
     * @param i Array if Entities to be Drawn.
     */
    public void draw(Entity[] i){
        if(i == null) return;
        for(Entity e: i){
            t.translate(e.translation);
            t.rotate(e.rotation);
            t.scale(e.scale);
            currshader.updateTransformation(t.calculateTransformation());
            e.draw();
        }
    }
}