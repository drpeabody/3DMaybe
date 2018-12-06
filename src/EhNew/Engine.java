package EhNew;

import EhNew.math.PerspectiveProjection;
import EhNew.math.Projection;
import EhNew.util.TickManager;
import EhNew.util.Camera;
import EhNew.math.Vec2;
import EhNew.shaders.FactoryShader;
import EhNew.shaders.Shader;
import EhNew.util.TextFactory;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;

/**
 * @since 20 Jan, 2016
 * @author Abhishek
 */
public class Engine {
    private final int drawMode;
    private int fps;
    private long window;
    protected Camera c;
    private Level current;
    private TickManager updater, renderer;
    protected TextFactory text;
    protected Shader currshader;
    
    
    /**
     * Standard Constructor, makes no OpenGL calls therefore doesn't need a hardware 
     * accelerated context.
     */
    public Engine(){
        window = -1;
        drawMode = GL_TRIANGLES;
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
        this.fps = f;
        System.out.println("ENGINE: Initiating...");
        try {
            renderer = new TickManager(() -> {
                if (!glfwInit()) {
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
                c = new Camera();
                PerspectiveProjection p = new PerspectiveProjection();
                p.setAspectRatio( (float)width / (float)height );
                p.setWidth( (float)width );
                p.calculateProjection();
                c.setProjection(p);
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
        int[] w = new int[1], h = new int[1];
        glfwGetFramebufferSize(window, w, h);
        Projection p = c.getProjection();
        p.setWidth(w[0]);
        p.setAspectRatio((float)w[0]/h[0]);
        glViewport(0, 0, w[0], h[0]);
        currshader.updateProjection(p.calculateProjection().getProjectionMatrix());
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
        if (glfwWindowShouldClose(window)) {
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
    public Vec2 getMousePosinOpenGLCoods(){
        double mouseX[] = new double[1], mouseY[] = new double[1];
        GLFW.glfwGetCursorPos(window, mouseX, mouseY);
        Vec2 size = c.getProjection().getScreenSizePixels();
        return new Vec2(
            ((float)mouseX[0])*2/size.x - 1f, 1f + (1f - (float)mouseY[0])*2/size.y);
    }
        
    /**
     * Assuming basic shader functionality to be available in the current Shader, 
     * this function updates the standard Transformation Matrices, also called the 
     * Model Matrices. It then makes uniform calls on the shader to update the Matrix
     * and draws the given Entity with the current DrawMode.
     * @param e Entity to be drawn
     */
    public void draw(Entity e){
        currshader.updateTransformationVectors(e.calculateTransformation());
        e.draw(drawMode);
    }
    
    
}