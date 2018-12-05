package EhNew.util.HUD;

import EhNew.math.Vec2;
import EhNew.math.Vec4;
/**
 * @since Jun 22, 2017
 * @author Abhishek
 */
public class HUDVertex {
    public static final int ATTRIB_POSITION_INDEX = 0;
    public static final int ATTRIB_TEXCOOD_INDEX = 1;
    public static final int ATTRIB_COLOR_INDEX = 2;
    public static final int ATTRIB_TEX_DOMINANCE = 3;
    
    public static final int SIZE = 2*4 + 2*4 + 4*4 + 4; //Stores the Number of Bytes
    
    public static final int OFFSET_POSITION = 0;
    public static final int OFFSET_TEXCOOD = 2*4;
    public static final int OFFSET_COLOR = 2*4 + 2*4;
    public static final int OFFSET_TEX_DOM = 2*4 + 2*4 + 4*4;
    
    Vec2 pos;
    Vec2 textCood;
    Vec4 color;
    float texDom;
    
    public HUDVertex(){
        pos = new Vec2();
        textCood = new Vec2();
        color = new Vec4();
        texDom = 1f;
    }
    
    public HUDVertex(Vec2 p, Vec2 t, Vec4 c, float texDom){
        pos = p;
        textCood = t;
        color = c;
        this.texDom = texDom;
    }

    public int getNumberOfFloats(){
        return SIZE / 4;
    }

    public Vec2 getPos() {
        return pos;
    }

    public Vec4 getColor() {
        return color;
    }

    public float getTexDom() {
        return texDom;
    }
    
    public float[] getArray(){
        return new float[]{
            pos.x, pos.y,
            textCood.x, textCood.y,
            color.r, color.g, color.b, color.a,
            texDom
        };
    }
}
