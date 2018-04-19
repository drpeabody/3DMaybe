package EhNew.geom;

import EhNew.DrawableEntity;
import EhNew.math.Vec2;
import EhNew.math.Vec3;
import EhNew.shaders.CamShader;
import EhNew.shaders.Shader;
import EhNew.util.Camera;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

/**
 * @since Apr 14, 2018
 * @author Abhishek
 */
public abstract class Sprite extends DrawableEntity{
    Camera c;

    public <T extends Shader & CamShader>Sprite(T s, Camera c) {
        super(s);
        this.c = c;
    }

    @Override
    public void load() {        
        Vertex v[] = new Vertex[]{new Vertex(), new Vertex(), new Vertex(), new Vertex()};
        int arr[] = new int[]{0,1,2,2,3,0,0,3,2,2,1,0};
        
        v[0].TextCoods = new Vec2(0f, 0f);
        v[1].TextCoods = new Vec2(1f, 0f);
        v[2].TextCoods = new Vec2(1f, 1f);
        v[3].TextCoods = new Vec2(0f, 1f);
        
        v[0].normal = new Vec3(0f, 0f, 1f);
        v[1].normal = new Vec3(0f, 0f, 1f);
        v[2].normal = new Vec3(0f, 0f, 1f);
        v[3].normal = new Vec3(0f, 0f, 1f);
        
        v[0].tangent = new Vec3(0f, 1f, 0f);
        v[1].tangent = new Vec3(0f, 1f, 0f);
        v[2].tangent = new Vec3(0f, 1f, 0f);
        v[3].tangent = new Vec3(0f, 1f, 0f);
        
        v[0].pos = new Vec3(0f, 0f, 0f);
        v[1].pos = new Vec3(1f, 0f, 0f);
        v[2].pos = new Vec3(1f, 1f, 0f);
        v[3].pos = new Vec3(0f, 1f, 0f);
        
        IntBuffer idx = Vertex.getDataFrom(arr);
        FloatBuffer vert = Vertex.getDataFrom(v);
        idx.flip();
        vert.flip();
        
        indexCount = idx.limit();
        indexOffset = 0;
        idxID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, idx, GL_STATIC_DRAW);

        vertID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertID);
        glBufferData(GL_ARRAY_BUFFER, vert, GL_STATIC_DRAW);
    }
    
    public CamShader getCamChader(){
        return (CamShader)s;
    }

    //Texture binding needs to be done manually. Then just call this draw function.
    //Do not forget to unbind your textures.
    @Override
    public void draw(int drawMode) {
        bindTextures();
//        Vec3 temp = c.getTarget();
//        c.setTarget(translation.difference(c.getPos()));
//        c.updateCameraMatrices(getCamChader());
        super.draw(drawMode);
//        c.setTarget(temp);
//        c.updateCameraMatrices(getCamChader());
    }

    @Override
    public void destroy() {
        super.destroy();
    }
    
    //Texture binding needs to be done manually.
    public abstract void bindTextures();
}
