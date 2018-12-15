package EhNew.util;

import EhNew.Engine;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL13;

/**
 * @since 28 Jan, 2016
 * @author Abhishek
 */
public class Texture {
    private int id, target, unit, numComp;
    private BufferedImage image;
    private ByteBuffer data;
    private int height, width;
    
    public Texture(int TextureTarget, int textureUnit, String fileName){
        id = -1;
        unit = textureUnit;
        target = TextureTarget;
        image = null;
        data = null;
        numComp = 0;
        if(fileName == null) fileName = "null.png";
        try{
            image = ImageIO.read(Engine.class.getResourceAsStream(fileName));
        } catch (IOException | IllegalArgumentException ex) {
            try {
                System.out.println("Specified TextureFile not Found, Reading default Texture");
                image = ImageIO.read(Engine.class.getResourceAsStream("null.png"));
            } catch (IOException ignored) {
                System.out.println("Fatal Error in reading System File.");
                System.exit(1);
            }
        }
        height = image.getHeight();
        width = image.getWidth();
    }
    public Texture(int TextureTarget, int textureUnit, BufferedImage b){
        id = -1;
        unit = textureUnit;
        target = TextureTarget;
        if(b == null){
            throw new NullPointerException("Buffer cannot be Null");
        }
        numComp = b.getColorModel().getNumComponents();
        image = b;
        height = b.getHeight();
        width = b.getWidth();
        data = null;
    }
    
    public void loadFromImage() {
        if(image == null) throw new IllegalStateException("Attempting to load Texture from null Image.");
        numComp = image.getColorModel().getNumComponents();
        width = image.getWidth();
        height = image.getHeight();
        byte[] b = new byte[numComp * height * width];
        image.getRaster().getDataElements(0, 0, width, height, b);
        data = BufferUtils.createByteBuffer(b.length).put(b);
        data.flip();
    }

    public int getNumComp() {
        return numComp;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }

    //This function needs to be optimised for multiple runs
    public void bufferData(){
        GL13.glActiveTexture(unit);
        id = glGenTextures();
        glBindTexture(target, id);
        glTexImage2D(target, 0, (numComp == 3) ? GL_RGB : GL_RGBA, width, height,
                0, (numComp == 3) ? GL_RGB : GL_RGBA, GL_UNSIGNED_BYTE, data);
        glTexParameterf(target, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(target, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
    }
    public void changeImageTo(BufferedImage b){
        image = b;
    }
    
    public void destroy(){
        glDeleteTextures(id);
    }
    public void bind(){
        GL13.glActiveTexture(unit);
        glBindTexture(target, id);
    }
    public void unBind(){
        GL13.glActiveTexture(unit);
        glBindTexture(target, -1);
    }
    
    public void changeBindingTo(int textureUnit){
        unit = textureUnit;
    }
}