package EhNew.math;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

/**
 * @since 22 Jan, 2016
 * @author Abhishek
 */
public class Matrix4f {
    float[] m;
    static final float identity[] = new float[]{
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f};
    //Keeping the identity Matrix in a buffer is more efficient than keeping an array and constructing a buuffer.
    
    public Matrix4f(){
        m = new float[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
    }
    public Matrix4f(Vec4 v1, Vec4 v2, Vec4 v3, Vec4 v4){
        m = new float[]{v1.r, v1.g, v1.b, v1.a, v2.r, v2.g, v2.b, v2.a, v3.r, v3.g, v3.b, v3.a, v4.r, v4.g, v4.b, v4.a};
    }
    
    public float[] getArray(){
        return m;
    }
    public FloatBuffer getBuffer(){
        FloatBuffer b = BufferUtils.createFloatBuffer(16);
        return (FloatBuffer)b.put(m).flip();
    }
    public static FloatBuffer getIndentityBuffer(){
        FloatBuffer f = BufferUtils.createFloatBuffer(16).put(identity);
        f.flip();
        return f;
    }
}