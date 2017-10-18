package EhNew.util.HUD;

import EhNew.math.Vec2;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;

/**
 * @since 26 Apr, 2017
 * @author Abhishek
 */
public abstract class HUDElement {
    //The coordinates are in screen space clamed to -1.0 to 1.0 for both x and y axes
    //The z component is ignored completey
    
    protected HUDVertex BL, BR, TL, TR;
    protected int bufferOffset;//Follows bytes
    protected HUDBuffer buffer;
    
    public HUDElement(){
        BL = new HUDVertex();
        BR = new HUDVertex();
        TL = new HUDVertex();
        TR = new HUDVertex();
        BL.textCood = new Vec2(0, 1);
        BR.textCood = new Vec2(1, 1);
        TL.textCood = new Vec2(0, 0);
        TR.textCood = new Vec2(1, 0);
        bufferOffset = 0;
    }
    
    public void load(HUDBuffer b){
        buffer = b;
        bufferOffset = b.getCurrentOffset();
        buffer.bind();
        buffer.addData(TR.getArray());
        buffer.addData(BR.getArray());
        buffer.addData(BL.getArray());
        buffer.addData(TL.getArray());
    }
    
    public void draw() {
        buffer.draw(bufferOffset);
    }
    
    public void updateBuffer(){
        buffer.bind();
        FloatBuffer f = BufferUtils.createFloatBuffer(HUDVertex.SIZE*4/4)
                .put(TR.getArray())
                .put(BR.getArray())
                .put(BL.getArray())
                .put(TL.getArray());
        f.flip();
        GL15.glBufferSubData(GL_ARRAY_BUFFER, bufferOffset, f);
    }
}
