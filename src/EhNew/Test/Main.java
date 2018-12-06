package EhNew.Test;

import EhNew.Engine;
import EhNew.Level;
import EhNew.geom.Axes;
import EhNew.shaders.FactoryShader;
import EhNew.data.Arrow;
import EhNew.geom.Cube;
import EhNew.data.DirectionalLight;
import EhNew.util.HUD.*;
import EhNew.data.PointLight;
import EhNew.geom.Sphere;
import EhNew.geom.Terrain;
import EhNew.shaders.HUDShader;
import EhNew.math.Vec2;
import EhNew.math.Vec3;
import EhNew.math.Vec4;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL15;


/**
 * @since 28 Jan, 2016
 * @author Abhishek
 */
public class Main {

    public static void main(String[] args) {
        Engine e = new Engine();
        e.init("Window Title", 1280, 720, 60);
        LevelSample l = new LevelSample(e);
        e.loadLevel(l);
        e.start(l);
    }
    
    static class LevelSample extends Level{
        PictureBox healthLabel, armorLabel;
        HUDShader hs;
        HUDBuffer hud;
        FactoryShader f;
        ProgressBar health, armor;

        Axes ax;
        Terrain ter;
        Cube sc2;
        Sphere s1, s2;
        PointLight l,m;
        Arrow a;
        GLFWCursorPosCallback cursor;
        GLFWKeyCallback key;
        
        public LevelSample(Engine e){
            super(e);
            f = (FactoryShader)engine.getShader();
            engine.getTextFactory().setSize(135);
            engine.getTextFactory().setColor(new Vec4(0.3f,0.7f,0.1f,1f));
            healthLabel = new PictureBox(new Vec2(-0.9f, -1f), new Vec2(0.25f,0.2f), 
                    engine.getTextFactory().createText("Health"), new Vec4(0f, 0f, 0f, 0f));
            engine.getTextFactory().setColor(new Vec4(.1f,0.5f,1f,1f));
            armorLabel = new PictureBox(new Vec2(-0.9f, -0.8f), new Vec2(0.25f,0.2f), 
                    engine.getTextFactory().createText("Armor"), new Vec4(0f, 0f, 0f, 0f));
            hs = new HUDShader();
            hud = new HUDBuffer(4, GL15.GL_DYNAMIC_DRAW);
            armor = new ProgressBar(new Vec2(-0.6f, -0.75f), new Vec2(0, -0.65f), new Vec4(.1f,0.5f,1f,1f), 0.9f, 1f, null);
            health = new ProgressBar(new Vec2(-.6f, -.95f), new Vec2(0, -.85f), new Vec4(.3f, .6f, 0f, 1f), 0.9f, 1f, null);
            ter = new Terrain(null, new Vec3(3f, 0.00001f, 3f), new Vec3(-60f, -2f, -60f), new Vec2(0.05f, 0.05f), f);
            s1 = new Sphere(f);
            s2 = new Sphere(f);
            a = new Arrow(f);
            sc2 = new Cube(f);
            l = new PointLight();
            m = new PointLight();
            ax = new Axes(f);
        }

        @Override
        public void load() {
            hud.load();
            hs.init();
            hs.loadShader();
            
            glfwSetInputMode(engine.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            engine.getCamera().setPos(new Vec3(0f, 0f, -7f));
            glfwSetCursorPosCallback(engine.getWindow(), cursor = engine.getCamera().getMousePosCallback());
            glfwSetKeyCallback(engine.getWindow(), key = engine.getCamera().getKeyCallBack());
            glfwSetFramebufferSizeCallback(engine.getWindow(), new GLFWFramebufferSizeCallback() {
                @Override
                public void invoke(long l, int w, int h) {
                    engine.updateFrameSize();
                }
            });
            
            engine.switchToShader(hs);
            health.load(hud);
            armor.load(hud);
            healthLabel.load(hud);
            armorLabel.load(hud);
            s1.load();
            s2.load();
            sc2.load();
            a.load();
            ax.load();
            ter.load();
            
            a.translateBy(new Vec3(0f, 0f, 2f));
            s2.translateBy(new Vec3(0f, 0f, 5f));
            s1.translateBy(new Vec3(0f, 5f, 1f));
            
            sc2.setTranslation(new Vec3(4f, 0, 1.5f));
            sc2.setScale(new Vec3(2f, 2f, 2f));
            sc2.rotateBy(new Vec3(0.5f, 0.0f, 0f));
            
            l.diffInten = 1f;
            l.color.x = 1f;
            l.color.y = 0.2f;
            l.color.z = 0.2f;
            l.pos.z = 10.1f;
            l.pos.y = -4f;
            
            m.diffInten = 1f;
            m.color.x = 0.2f;
            m.color.y = 0.2f;
            m.color.z = 1f;
            m.pos.z = -10.0f;
            m.pos.y = -4f;
            
            engine.switchToShader(f);
            f.updateDirectionalLighting(new DirectionalLight());
            l.setIdx(f.addPointLight(l));
            m.setIdx(f.addPointLight(m));
            f.finalizePointLights();
            
            engine.getCamera().updateProjection(f);
            engine.getCamera().updateCameraMatrices(f);
            
            glClearColor(0.2f, 0.2f, 0.4f, 1.0f);
        }

        @Override
        public void draw() {
            //A suggested thing would be to use shader managers
            //to swithc between shaders as engine keeps no references for future use.
            engine.switchToShader(f);
            //engine must be called upon to draw objects and it handles shaders
            //to correctly implement transformation for each entity without the user having to worry about it
            //Passing an array of a set of large objects will speed up this thing

            engine.draw(sc2);
            engine.draw(s1);
            engine.draw(s2);
            engine.draw(a);
            engine.draw(ax);
            engine.draw(ter);
            
            f.updatePointLightProperty(l, f.UNIFORM_LIGHT_POSITION);
            f.updatePointLightProperty(m, f.UNIFORM_LIGHT_POSITION);
            
            engine.getCamera().updateCameraMatrices(f);
            
            engine.switchToShader(hs);
            healthLabel.draw();
            health.draw();
            armor.draw();
            armorLabel.draw();
        }

        @Override
        public void update() {
            l.pos.z -= 0.01f;
            m.pos.z += 0.01f;
            engine.getCamera().update();
        }

        @Override
        public void destroy() {
            System.out.println("Performing Destruction");
            cursor.free();
            a.destroy();
            s1.destroy();
            ter.destroy();
            s2.destroy();
            sc2.destroy();
            ax.destroy();
            f.destroyShaders();
            hs.destroyShaders();
            hud.release();
            healthLabel.destroy();
            armorLabel.destroy();
            armor.destroy();
        }
    }
}