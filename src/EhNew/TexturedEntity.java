package EhNew;

import EhNew.shaders.Shader;
import EhNew.util.Texture;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

/**
 * @since 6 Dec, 2018
 * @author Abhishek
 */
public class TexturedEntity extends DrawableEntity{
    private Texture diffuse, normal, emmisive;

    public TexturedEntity(Shader s){
        super(s);
        diffuse = new Texture(GL_TEXTURE_2D, s.getDiffuseMapTextureUnit(), "null.png");
        normal = new Texture(GL_TEXTURE_2D, s.getNormalMapTextureUnit(),"nullN.png");
        emmisive = new Texture(GL_TEXTURE_2D, s.getEmmisiveMapTextureUnit(),"nullE.png");
    }
    public TexturedEntity(Shader s, Texture diffuse, Texture normal, Texture emmissive){
        super(s);
        this.diffuse = diffuse;
        this.normal = normal;
        this.emmisive = emmissive;
    }

    @Override
    public void draw() {
        normal.bind();
        diffuse.bind();
        emmisive.bind();
        super.draw();
        normal.unBind();
        emmisive.unBind();
        diffuse.unBind();
    }

    @Override
    public void load() {
        diffuse.loadFromImage();
        diffuse.bufferData();
        normal.loadFromImage();
        normal.bufferData();
        emmisive.loadFromImage();
        emmisive.bufferData();
    }
    @Override
    public void destroy() {
        super.destroy();
        diffuse.destroy();
        normal.destroy();
        emmisive.destroy();
    }
}