package EhNew.geom;

import EhNew.TexturedEntity;
import EhNew.shaders.Shader;

/**
 * @author Abhishek
 */
public class Sphere extends TexturedEntity {
    
    public Sphere(Shader s){
        super(s);
    }

    @Override
    public void load() {
        super.load();
        load(Sphere.class.getResourceAsStream("sphere.obj"));
    }
}
