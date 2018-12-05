package EhNew.util.HUD;

import EhNew.util.Texture;
import EhNew.math.Vec2;
import EhNew.math.Vec4;
import java.awt.image.BufferedImage;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * @since 28 Apr, 2017
 * @author Abhishek
 */
public class PictureBox extends HUDElement{
    private boolean isTextureDirty;
    private Texture t;
    
    public PictureBox(Vec2 pos, Vec2 size, BufferedImage text, Vec4 color){
        BL.pos.x = pos.x;
        BL.pos.y = pos.y;
        BR.pos.x = pos.x + size.x;
        BR.pos.y = pos.y;
        TL.pos.x = pos.x;
        TL.pos.y = pos.y + size.y;
        TR.pos.x = pos.x + size.x;
        TR.pos.y = pos.y + size.y;
        if(text == null){
            TL.texDom = TR.texDom = BR.texDom = BL.texDom = 0f;
            BufferedImage b = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
            t = new Texture(GL11.GL_TEXTURE_2D, GL13.GL_TEXTURE10, b);
        }
        else{
            t = new Texture(GL11.GL_TEXTURE_2D, GL13.GL_TEXTURE0, text);
        }
        isTextureDirty = true;
        isBufferDirty = false;
        BL.color = BR.color = TR.color = TL.color;
        TL.color.r = color.r;
        TL.color.g = color.g;
        TL.color.b = color.b;
        TL.color.a = color.a;
    }
    
    @Override
    public void load(HUDBuffer b) {
        super.load(b);
        t.loadFromImage();
        t.bufferData();
        isTextureDirty = false;
    }

    public void updatePicture(BufferedImage b){
        if(b == null){
            b = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            b.setRGB(0, 0, 0);
            changeTextureDominance(0f);
        }
        t.changeImageTo(b);
        t.loadFromImage();
        isTextureDirty = true;
    }

    @Override
    public void draw() {
        if (isTextureDirty) {
            isTextureDirty = false;
            t.bufferData();
        }
        t.bind();
        super.draw();
        t.unBind();
    }
    
    @Override
    public void destroy() {
        if(t != null) t.destroy();
    }
}
