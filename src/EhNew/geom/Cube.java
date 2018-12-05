package EhNew.geom;

import EhNew.DrawableEntity;
import EhNew.shaders.Shader;
import EhNew.util.Texture;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

/**
 * @since 22 Jan, 2016
 * @author Abhishek
 */
public class Cube extends DrawableEntity{
    private Texture t, normal, emmisive;
    
    public Cube(Shader s){
        super(s);
        t = new Texture(GL_TEXTURE_2D, s.getDiffuseMapTextureUnit(), "null.png");
        normal = new Texture(GL_TEXTURE_2D, s.getNormalMapTextureUnit(),"nullN.png");
        emmisive = new Texture(GL_TEXTURE_2D, s.getEmmisiveMapTextureUnit(),"nullE.png");
    }
    
    @Override
    public void draw(int drawMode) {
        normal.bind();
        t.bind();
        emmisive.bind();
        super.draw(drawMode);
    }

    @Override
    public void load() {
        t.loadFromImage();
        t.bufferData();
        normal.loadFromImage();
        normal.bufferData();
        emmisive.loadFromImage();
        emmisive.bufferData();
        load(Cube.class.getResourceAsStream("cube.obj"));
        t.unBind();
    }
    @Override
    public void destroy() {
        super.destroy();
        t.destroy();
        normal.destroy();
        emmisive.destroy();
    }
}