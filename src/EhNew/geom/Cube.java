package EhNew.geom;

import EhNew.TexturedEntity;
import EhNew.shaders.Shader;

/**
 * @since 22 Jan, 2016
 * @author Abhishek
 */
public class Cube extends TexturedEntity {
    
    public Cube(Shader s){
        super(s);
    }

    @Override
    public void load() {
        super.load();
        load(Cube.class.getResourceAsStream("cube.obj"));
    }
}
