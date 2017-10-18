package EhNew.geom;

import EhNew.math.Transformer;
import EhNew.math.Vec3;
import EhNew.shaders.FactoryShader;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;

/**
 * @since Jul 12, 2017
 * @author Abhishek
 */
public class TerrainFoilageBuffer {
    Terrain ter;
    Transformer trans;
    int filled;//Follows Number of Bytes
    int bufferID, size;//Size follows number of floats
    int numInstance;
    Foilage f;
    
    public static final int MAT_SIZE_FLOATS = 16;//number of floats per matrix
    
    //MESH - PORJ - PROJ - PROJ - PROJ - PROJ - ...
    /*
        MESH - numVerts * Vertex.SIZE
        PORJ - numInstace * flotasPerInstance
    */
    
    public TerrainFoilageBuffer(Terrain ter, int numInstances, Foilage f){
        this.ter = ter;
        trans = new Transformer();
        bufferID = -1;
        size = f.getFloatsPerInstanceVertData() + numInstances * MAT_SIZE_FLOATS;
        numInstance = numInstances;
        filled = f.getFloatsPerInstanceVertData() * 4;//Allows us to directly add tranform data to buffer
        this.f = f;
    }
    
    public void load() {
        if (bufferID != -1) {
            throw new IllegalStateException("Buffer already loaded!");
        }
        bufferID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, new float[size], GL15.GL_DYNAMIC_DRAW);
        FloatBuffer vert = Vertex.getDataFrom(f.getVertices());
        vert.flip();
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vert);
    }
    
    public void bind(){
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
    }
    public void addData(Vec3 tran, Vec3 rot, Vec3 scale){
        trans.translate(tran);
        trans.rotate(rot);
        trans.scale(scale);
        bind();
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, filled, trans.calculateTransformation());
        filled += MAT_SIZE_FLOATS * 4;//The Matrices come with Pre-amped 4th indices matrix
    }
    public void setMesh(Foilage f){
        this.f = f;
        size = f.getFloatsPerInstanceVertData() + numInstance * MAT_SIZE_FLOATS;
        filled = f.getFloatsPerInstanceVertData() * 4;
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, Vertex.getDataFrom(f.getVertices()));
    }
    public void changeMesh(Foilage f){
        this.f = f;
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, Vertex.getDataFrom(f.getVertices()));
    }
    
    public void release(){
        GL15.glDeleteBuffers(bufferID);
    }

    public void drawAll(FactoryShader s){
        int pos = s.getAttribLocation(FactoryShader.ATTRIB_INSTANCE_TRANSFORM_MATRIX);
        int pos1 = pos + 0;
        int pos2 = pos + 1;
        int pos3 = pos + 2;
        int pos4 = pos + 3;
        GL20.glEnableVertexAttribArray(pos1);
        GL20.glEnableVertexAttribArray(pos2);
        GL20.glEnableVertexAttribArray(pos3);
        GL20.glEnableVertexAttribArray(pos4);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        GL20.glVertexAttribPointer(pos1, 4, GL11.GL_FLOAT, false, MAT_SIZE_FLOATS * 4, 0);
        GL20.glVertexAttribPointer(pos2, 4, GL11.GL_FLOAT, false, MAT_SIZE_FLOATS * 4, 16);//Second row of the matrix will be found 16 bytes later
        GL20.glVertexAttribPointer(pos3, 4, GL11.GL_FLOAT, false, MAT_SIZE_FLOATS * 4, 32);//Thrid row will be found 32 bytes later
        GL20.glVertexAttribPointer(pos4, 4, GL11.GL_FLOAT, false, MAT_SIZE_FLOATS * 4, 48);
        
        GL33.glVertexAttribDivisor(pos1, 1);
        GL33.glVertexAttribDivisor(pos2, 1);
        GL33.glVertexAttribDivisor(pos3, 1);
        GL33.glVertexAttribDivisor(pos4, 1);
        
        GL20.glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_POSITION);
        GL20.glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_TEXTURE_COOD);
        GL20.glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_NORMAL);
        GL20.glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_TANGENT);
        
        GL20.glVertexAttribPointer(Vertex.POINTER_ATTRIB_POSITION, 3, GL11.GL_FLOAT, false, 0, Vertex.OFFSET_POSITION);
        GL20.glVertexAttribPointer(Vertex.POINTER_ATTRIB_TEXTURE_COOD, 2, GL11.GL_FLOAT, false, 0, Vertex.OFFSET_TEXTURE_COORD);
        GL20.glVertexAttribPointer(Vertex.POINTER_ATTRIB_NORMAL, 3, GL11.GL_FLOAT, false, 0, Vertex.OFFSET_NORMAL);
        GL20.glVertexAttribPointer(Vertex.POINTER_ATTRIB_TANGENT, 3, GL11.GL_FLOAT, false, 0, Vertex.OFFSET_TANGENT);
        
        GL31.glDrawArraysInstanced(GL11.GL_QUADS, 0 , 1, numInstance);
        
        GL20.glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_POSITION);
        GL20.glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_TEXTURE_COOD);
        GL20.glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_NORMAL);
        GL20.glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_TANGENT);
        GL20.glDisableVertexAttribArray(pos1);
        GL20.glDisableVertexAttribArray(pos2);
        GL20.glDisableVertexAttribArray(pos3);
        GL20.glDisableVertexAttribArray(pos4);
                
        GL15.glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
