package EhNew.data;

import EhNew.DrawableEntity;
import EhNew.shaders.Shader;
import org.lwjgl.opengl.GL11;

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
    public void draw() {
        super.draw(GL11.GL_TRIANGLES);
    }
}