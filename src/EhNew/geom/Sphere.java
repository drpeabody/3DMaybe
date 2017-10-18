package EhNew.geom;

import EhNew.DrawableEntity;
import org.lwjgl.opengl.GL11;

/**
 * @author Abhishek
 */
public class Sphere extends DrawableEntity {
    
    public Sphere(){
        super();
    }

    @Override
    public void load() {
        load(Sphere.class.getResourceAsStream("sphere.obj"));
    }

    @Override
    public void draw() {
        super.draw(GL11.GL_TRIANGLES);
    }
    
    
}
