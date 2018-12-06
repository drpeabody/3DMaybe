package EhNew.util.HUD;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import org.lwjgl.opengl.GL20;

/**
 * @since 21 Jun, 2017
 * @author Abhishek
 */
public class HUDBuffer {
    //Since All openGL draw must use a buffer, this class provides an interface 
    //for buffereing all the HUDElements without having to buffer them individually.

    private final int size;//follows no of floats where 1 HUDVertex = 9 floats
    private int usage;
    private int filled;//follows no of bytes
    private int id;
    
    public HUDBuffer(int size, int usage){
        this.size = size * HUDVertex.SIZE * 4;
        id = -1;
        this.usage = usage;
        filled = 0;
    }
    
    public int getCurrentOffset(){
        return filled;
    }
    public int getBufferID(){
        return id;
    }
    public int getBufferUsage(){
        return usage;
    }
    public int getBufferSize(){
        return size;
    }
    
    public void bind(){
        if(id == -1) throw new IllegalStateException("Hud Buffer not loaded!");
        GL15.glBindBuffer(GL_ARRAY_BUFFER, id);
    }
    
    public void load(){
        if(id != -1) throw new IllegalStateException("Buffer Already Loaded!");
        id = GL15.glGenBuffers();
        GL15.glBindBuffer(GL_ARRAY_BUFFER, id);
        GL15.glBufferData(GL_ARRAY_BUFFER, new float[size], usage);
    }
    
    public void addData(float[] f){
        if(f.length + filled/4 > size) throw new ArrayIndexOutOfBoundsException("Buffer Overflow");
        GL15.glBufferSubData(GL_ARRAY_BUFFER, filled, f);
        filled += f.length*4;
    }
    
    public void release(){
        GL15.glDeleteBuffers(id);
        id = -1;
    }
    
    public void draw(int offset){
        GL20.glEnableVertexAttribArray(HUDVertex.ATTRIB_POSITION_INDEX);
        GL20.glEnableVertexAttribArray(HUDVertex.ATTRIB_TEXCOOD_INDEX);
        GL20.glEnableVertexAttribArray(HUDVertex.ATTRIB_COLOR_INDEX);
        GL20.glEnableVertexAttribArray(HUDVertex.ATTRIB_TEX_DOMINANCE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        GL15.glBindBuffer(GL_ARRAY_BUFFER, id);
        
        GL20.glVertexAttribPointer(HUDVertex.ATTRIB_POSITION_INDEX, 2, GL11.GL_FLOAT, true, HUDVertex.SIZE, HUDVertex.OFFSET_POSITION);
        GL20.glVertexAttribPointer(HUDVertex.ATTRIB_TEXCOOD_INDEX, 2, GL11.GL_FLOAT, true, HUDVertex.SIZE, HUDVertex.OFFSET_TEXCOOD);
        GL20.glVertexAttribPointer(HUDVertex.ATTRIB_COLOR_INDEX, 4, GL11.GL_FLOAT, true, HUDVertex.SIZE, HUDVertex.OFFSET_COLOR);
        GL20.glVertexAttribPointer(HUDVertex.ATTRIB_TEX_DOMINANCE, 1, GL11.GL_FLOAT, true, HUDVertex.SIZE, HUDVertex.OFFSET_TEX_DOM);
        
        GL11.glDrawArrays(GL11.GL_QUADS, offset/HUDVertex.SIZE, 4);
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL20.glDisableVertexAttribArray(HUDVertex.ATTRIB_POSITION_INDEX);
        GL20.glDisableVertexAttribArray(HUDVertex.ATTRIB_TEXCOOD_INDEX);
        GL20.glDisableVertexAttribArray(HUDVertex.ATTRIB_COLOR_INDEX);
        GL20.glDisableVertexAttribArray(HUDVertex.ATTRIB_TEX_DOMINANCE);
        
        GL15.glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
