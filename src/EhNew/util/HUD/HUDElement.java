package EhNew.util.HUD;

import EhNew.math.Vec2;
import EhNew.math.Vec4;
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
    protected boolean isBufferDirty, toBeDestroyed;
    
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
        isBufferDirty = toBeDestroyed = false;
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
        if(toBeDestroyed){
            destroy();
            return;
        }
        if(isBufferDirty){
            isBufferDirty = false;
            updateBuffer();
        }
        buffer.draw(bufferOffset);
    }
    
    public void updateBuffer(){
        buffer.bind();
        int numfloats = HUDVertex.SIZE / 4;
        float f[] = new float[numfloats * 4];
        System.arraycopy(TR.getArray(), 0, f, 0, numfloats);
        System.arraycopy(BR.getArray(), 0, f,        numfloats, numfloats);
        System.arraycopy(BL.getArray(), 0, f, 2 * numfloats, numfloats);
        System.arraycopy(TL.getArray(), 0, f, 3 * numfloats, numfloats);
        GL15.glBufferSubData(GL_ARRAY_BUFFER, bufferOffset, f);
    }
    
    public Vec2 getSize(){
        return TR.pos.difference(BL.pos);
    }
    
    public void changeTextureDominance(float f) {
        TL.texDom = TR.texDom = BR.texDom = BL.texDom = f;
        isBufferDirty = true;
    }
    
    public Vec2 getPosition(){
        return BL.pos;
    }
    public void movePositionBy(Vec2 dist){
        TL.pos = TL.pos.sum(dist);
        BL.pos = BL.pos.sum(dist);
        TR.pos = TR.pos.sum(dist);
        BR.pos = BR.pos.sum(dist);
        isBufferDirty = true;
    }
    public void movePositionTo(Vec2 newPos){
        Vec2 size = TR.pos.difference(BL.pos);
        BL.pos = new Vec2(newPos.x, newPos.y);
        TL.pos = new Vec2(newPos.x, newPos.y + size.y);
        TR.pos = new Vec2(newPos.x + size.x, newPos.y + size.y);
        BR.pos = new Vec2(newPos.x + size.x, newPos.y);
        isBufferDirty = true;
    }
    
    public void resizeTo(Vec2 newSize){
        Vec2 centre = new Vec2((TR.pos.x + BL.pos.x)/2, (TR.pos.y + BL.pos.y)/2);
        BL.pos = new Vec2(centre.x - newSize.x/2, centre.y - newSize.y/2);
        TL.pos = new Vec2(centre.x - newSize.x/2, centre.y + newSize.y/2);
        TR.pos = new Vec2(centre.x + newSize.x/2, centre.y + newSize.y/2);
        BR.pos = new Vec2(centre.x + newSize.x/2, centre.y - newSize.y/2);
        isBufferDirty = true;
    }
    public void resizeBy(Vec2 delta){
        Vec2 hs = TR.pos.difference(BL.pos).sum(delta);
        Vec2 centre = new Vec2((TR.pos.x + BL.pos.x)/2, (TR.pos.y + BL.pos.y)/2);
        BL.pos = new Vec2(centre.x - hs.x/2, centre.y - hs.y/2);
        TL.pos = new Vec2(centre.x - hs.x/2, centre.y + hs.y/2);
        TR.pos = new Vec2(centre.x + hs.x/2, centre.y + hs.y/2);
        BR.pos = new Vec2(centre.x + hs.x/2, centre.y - hs.y/2);
        isBufferDirty = true;
    }
    
    public void changeColor(Vec4 color){
        TL.color = TR.color = BR.color = BL.color = color;
        isBufferDirty = true;
    }
    
    public void prepareForDestruction(){
        toBeDestroyed = true;
    }
    
    public abstract void destroy();
    
    public boolean contains(Vec2 v){
        if(v.x >= BL.pos.x && v.y >= BL.pos.y){
            return v.x <= TR.pos.x && v.y <= TR.pos.y;
        }
        return false;
    }
    public boolean contains(float x, float y){
        if(x >= BL.pos.x && y >= BL.pos.x){
            return x <= TR.pos.x && y <= BR.pos.y;
        }
        return false;
    }
}
