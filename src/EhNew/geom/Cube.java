package EhNew.geom;

import EhNew.DrawableEntity;
import EhNew.util.Texture;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import org.lwjgl.opengl.GL13;

/**
 * @since 22 Jan, 2016
 * @author Abhishek
 */
public class Cube extends DrawableEntity{
    Texture t, normal;
    
    public Cube(){
        super();
        t = new Texture(GL_TEXTURE_2D, GL13.GL_TEXTURE0, "null.png");
        normal = new Texture(GL_TEXTURE_2D, GL13.GL_TEXTURE1,"nullN.png");
    }
    
    @Override
    public void draw() {
        normal.bind();
        t.bind();
        super.draw(GL11.GL_TRIANGLES);
    }
    
    @Override
    public void load() {
        t.loadFromImage();
        t.bufferData();
        normal.loadFromImage();
        normal.bufferData();
        load(Cube.class.getResourceAsStream("cube.obj"));
        t.unBind();
    }
    @Override
    public void destroy() {
        super.destroy();
        t.destroy();
        normal.destroy();
    }
}