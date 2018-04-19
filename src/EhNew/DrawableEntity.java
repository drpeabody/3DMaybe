package EhNew;

import EhNew.geom.Vertex;
import EhNew.shaders.Shader;
import EhNew.util.OBJLoader;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author Abhishek
 */
public abstract class DrawableEntity extends Entity{
    /* Shader information is required in the Drawable Entity as a measure for forward compatibilty.
     Future implementations of the DrawableEntity might want to bind specific textures to specific 
     Texture units, all of which information is contained in the Shader. DrawableEntity only knows what a particular 
     Texture will be used for, differently written shaders might have similar Texture Maps on different
     TExture Units.
    */
    protected Shader s;
    protected int vertID, idxID;
    protected int indexCount, indexOffset;
    
    public DrawableEntity(Shader s){
        super();
        this.s  = s;
        vertID = idxID = indexCount = indexOffset = -1;
    }
    
    @Override
    public void draw(int drawMode) {
        if((vertID + idxID) < 0){
            throw new IllegalStateException("Attempting to draw Entity without creation");
        }
        if((indexCount + indexOffset) < 1){
            throw new IllegalStateException("Attempting to draw unindexed Entity");
        }
        
        glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_POSITION);
        glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_TEXTURE_COOD);
        glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_NORMAL);
        glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_TANGENT);
        
        glBindBuffer(GL_ARRAY_BUFFER, vertID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxID);
        
        glVertexAttribPointer(Vertex.POINTER_ATTRIB_POSITION, 3, GL_FLOAT, false, Vertex.SIZE, Vertex.OFFSET_POSITION);
        glVertexAttribPointer(Vertex.POINTER_ATTRIB_TEXTURE_COOD, 2, GL_FLOAT, false, Vertex.SIZE, Vertex.OFFSET_TEXTURE_COORD);
        glVertexAttribPointer(Vertex.POINTER_ATTRIB_NORMAL, 3, GL_FLOAT, false, Vertex.SIZE, Vertex.OFFSET_NORMAL);
        glVertexAttribPointer(Vertex.POINTER_ATTRIB_TANGENT, 3, GL_FLOAT, false, Vertex.SIZE, Vertex.OFFSET_TANGENT);
        
        glDrawElements(drawMode, indexCount, GL_UNSIGNED_INT, indexOffset);
        
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_POSITION);
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_TEXTURE_COOD);
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_NORMAL);
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_TANGENT);
    }

    /*
    @Override
    public void drawInstanced(int drawMode, int countInstance) {
        if((vertID + idxID) < 0){
            throw new IllegalStateException("Attempting to draw Entity without creation");
        }
        if((indexCount + indexOffset) < 1){
            throw new IllegalStateException("Attempting to draw unindexed Entity");
        }
        
        glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_POSITION);
        glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_TEXTURE_COOD);
        glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_NORMAL);
        glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_TANGENT);
        
        glBindBuffer(GL_ARRAY_BUFFER, vertID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxID);
        
        glVertexAttribPointer(Vertex.POINTER_ATTRIB_POSITION, 3, GL_FLOAT, false, Vertex.SIZE, Vertex.OFFSET_POSITION);
        glVertexAttribPointer(Vertex.POINTER_ATTRIB_TEXTURE_COOD, 2, GL_FLOAT, false, Vertex.SIZE, Vertex.OFFSET_TEXTURE_COORD);
        glVertexAttribPointer(Vertex.POINTER_ATTRIB_NORMAL, 3, GL_FLOAT, false, Vertex.SIZE, Vertex.OFFSET_NORMAL);
        glVertexAttribPointer(Vertex.POINTER_ATTRIB_TANGENT, 3, GL_FLOAT, false, Vertex.SIZE, Vertex.OFFSET_TANGENT);
        
        GL31.glDrawElementsInstanced(drawMode, indexCount, GL_UNSIGNED_INT, indexOffset, countInstance);
        
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_POSITION);
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_TEXTURE_COOD);
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_NORMAL);
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_TANGENT);
        
    }*/
    
    public void load(InputStream s) {
        if (idxID != -1 || vertID != -1) {
            throw new IllegalStateException("Attempting to create pre-created Entity");
        }
        OBJLoader.Mesh e = OBJLoader.loadMesh((s));
        Vertex v[] = e.getVertexData();
        int i[] = e.getIndexData();
        
        Vertex.calcTangents(v, i, 3);
        
        IntBuffer idx = Vertex.getDataFrom(i);
        FloatBuffer vert = Vertex.getDataFrom(v);
        indexCount = idx.limit();

        idx.flip();
        vert.flip();
        indexOffset = 0;
        idxID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, idx, GL_STATIC_DRAW);

        vertID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertID);
        glBufferData(GL_ARRAY_BUFFER, vert, GL_STATIC_DRAW);
    }
    @Override
    public void destroy() {
        if(vertID == -1 || idxID == -1){
            throw new IllegalStateException("Entity isn't created");
        }
        glDeleteBuffers(vertID);
        glDeleteBuffers(idxID);
        vertID = -1;
        idxID = -1;
        indexCount = indexOffset = -1;
    }
}