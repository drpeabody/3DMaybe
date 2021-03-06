package EhNew;

import EhNew.geom.Vertex;
import EhNew.shaders.Shader;
import EhNew.util.OBJLoader;
import org.lwjgl.opengl.GL11;

import java.io.InputStream;
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
    private int vertID, idxID;
    private int indexCount, indexOffset;
    protected int drawMode;
    
    public DrawableEntity(Shader s){
        super();
        this.s  = s;
        vertID = idxID = indexCount = indexOffset = drawMode = -1;
    }

    public void draw(){
        s.updateTransformationVectors(calculateTransformation());

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

    public abstract void loadDataFrom(DrawableEntity d);
    protected void load(Shader s, int vertID, int idxID, int indexCount, int indexOffset, int drawMode){
        if (this.idxID != -1 || this.vertID != -1) {
            throw new IllegalStateException("Attempting to create pre-created Entity");
        }
        this.s = s;
        this.vertID = vertID;
        this.idxID = idxID;
        this.indexCount = indexCount;
        this.indexOffset = indexOffset;
        this.drawMode = drawMode;
    }
    public void load(InputStream s) {
        if (idxID != -1 || vertID != -1) {
            throw new IllegalStateException("Attempting to create pre-created Entity");
        }
        OBJLoader.Mesh e = OBJLoader.loadMesh((s));
        Vertex v[] = e.getVertexData();
        int i[] = e.getIndexData();
        
        Vertex.calcTangents(v, i, 3);
        if(drawMode == -1) drawMode = GL11.GL_TRIANGLES;
        load(Vertex.getDataFrom(v), i);
    }
    public void load(float vartices[], int indices[]){
        indexCount = indices.length;
        indexOffset = 0;
        
        idxID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        vertID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertID);
        glBufferData(GL_ARRAY_BUFFER, vartices, GL_STATIC_DRAW);
        
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

    public Shader getShader(){ return s; }
    public int getVertID() {
        return vertID;
    }
    public int getIndexCount() {
        return indexCount;
    }
    public int getIndexOffset() {
        return indexOffset;
    }
    public int getIdxID() {
        return idxID;
    }
    public int getDrawMode() { return drawMode; }

    public void update(){}
}