package EhNew.util.HUD;

import EhNew.Engine;
import EhNew.math.Vec2;
import EhNew.math.Vec4;
import java.awt.image.BufferedImage;

/**
 * @since 8 May, 2017
 * @author Abhishek
 */
public class ProgressBar extends PictureBox{
    Vec2 end;
    float progressX;//ranges from 0.0 to 1.0
    float progressY;//ranges from 0.0 to 1.0
    
    //Texture Coordinates
    //(0,0)   (1,0)
    //(0,1)   (1,1)

    //(0,0)   (pX,0)
    //(0,pY)  (pX,pY)
    
    public ProgressBar(Vec2 start, Vec2 end, Vec4 color, float pX, float pY, BufferedImage back){
        super(start, new Vec2(end.x - start.x, end.y - start.y), back, color);
        this.end = end;
        progressX = pX;
        progressY = pY;
    }

    public float getProgressX() {
        return progressX;
    }
    public float getProgressY() {
        return progressY;
    }
    public void setProgressX(float progressX) {
        this.progressX = progressX;
        refresh();
    }
    public void setProgressY(float progressY) {
        this.progressY = progressY;
        refresh();
    }
    
    public void changeProgressXBy(float f){
        progressX += f;
        refresh();
    }
    public void changeProgressYBy(float f){
        progressY += f;
        refresh();
    }
    
    public void refresh(){
        float sizeX = (end.x - BL.pos.x)*progressX;
        float sizeY = (end.y - BL.pos.y)*progressY;
        TL.pos.y = BL.pos.y + sizeY;
        TR.pos.y = BL.pos.y + sizeY;
        TR.pos.x = BL.pos.x + sizeX;
        BR.pos.x = BL.pos.x + sizeX;
        TR.textCood.x = progressX;
        BR.textCood.x = progressX;
        TL.textCood.y = 1 - progressY;
        TR.textCood.y = 1 - progressY;
//        updateBuffer();
        isBufferDirty = true;
   }
}
