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
    
}
