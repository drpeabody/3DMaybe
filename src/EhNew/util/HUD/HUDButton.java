package EhNew.util.HUD;

import EhNew.math.Vec2;
import EhNew.math.Vec4;
import java.awt.image.BufferedImage;

/**
 * @since Jul 5, 2017
 * @author Abhishek
 */
public class HUDButton extends PictureBox{
    
    Runnable r;

    public HUDButton(Vec2 pos, Vec2 size, BufferedImage text, Vec4 color, Runnable r) {
        super(pos, size, text, color);
        if(r == null){
            this.r = () -> {};
        }
        else{
            this.r = r;
        }
    }
    
    public void press(){
        r.run();
    }
    
    public boolean contains(Vec2 v){
        if(v.x >= BL.pos.x && v.y >= BL.pos.y){
            if(v.x <= TR.pos.x && v.y <= TR.pos.y){
                return true;
            }
        }
        return false;
    }
    public boolean contains(float x, float y){
        if(x >= BL.pos.x && y >= BL.pos.x){
            if(x <= TR.pos.x && y <= BR.pos.y){
                return true;
            }
        }
        return false;
    }
    
    public void changeColor(Vec4 color){
        TL.color = TR.color = BR.color = BL.color = color;
//        updateBuffer();
        isBufferDirty = true;
    }
}
