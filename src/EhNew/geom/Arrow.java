package EhNew.geom;

import EhNew.TexturedEntity;
import EhNew.shaders.Shader;

/**
 * @author Abhishek
 */
public class Arrow extends TexturedEntity {

    public Arrow(Shader s){
        super(s);
    }
    
    @Override
    public void load() {
        super.load();
        load(Arrow.class.getResourceAsStream("arrow.obj"));
    }
}
