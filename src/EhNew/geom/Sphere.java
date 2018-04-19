package EhNew.geom;

import EhNew.DrawableEntity;
import EhNew.shaders.Shader;

/**
 * @author Abhishek
 */
public class Sphere extends DrawableEntity {
    
    public Sphere(Shader s){
        super(s);
    }

    @Override
    public void load() {
        load(Sphere.class.getResourceAsStream("sphere.obj"));
    }

    @Override
    public void draw(int drawMode) {
        super.draw(drawMode);
    }
    
    
}
