package EhNew.data;

import EhNew.DrawableEntity;
import EhNew.shaders.Shader;

/**
 * @author Abhishek
 */
public class Arrow extends DrawableEntity{

    public Arrow(Shader s){
        super(s);
    }
    
    @Override
    public void load() {
        load(Arrow.class.getResourceAsStream("arrow.obj"));
    }
    
    
    @Override
    public void draw(int drawMode) {
        super.draw(drawMode);
    }
}