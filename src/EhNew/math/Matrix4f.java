package EhNew.math;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

/**
 * @since 22 Jan, 2016
 * @author Abhishek
 */
public class Matrix4f {
    FloatBuffer m;
    static FloatBuffer identity = BufferUtils.createFloatBuffer(16)
                .put(1.0f).put(0.0f).put(0.0f).put(0.0f)
                .put(0.0f).put(1.0f).put(0.0f).put(0.0f)
                .put(0.0f).put(0.0f).put(1.0f).put(0.0f)
                .put(0.0f).put(0.0f).put(0.0f).put(1.0f);
    
    public Matrix4f(){
        m = BufferUtils.createFloatBuffer(16);
    }
    public Matrix4f(Vec4 v1, Vec4 v2, Vec4 v3, Vec4 v4){
        m = BufferUtils.createFloatBuffer(16)
                .put(0.0f).put(0.0f).put(0.0f).put(0.0f)
                .put(0.0f).put(0.0f).put(0.0f).put(0.0f)
                .put(0.0f).put(0.0f).put(0.0f).put(0.0f)
                .put(0.0f).put(0.0f).put(0.0f).put(0.0f);
    }
    
    public FloatBuffer getBuffer(){
        return m;
    }
    public Matrix4f multiply(Matrix4f v){
        Matrix4f r = new Matrix4f();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                r.m.put((4*i)+j, m.get(4*i) * v.m.get(j)
                        + m.get(4*i + 1) * v.m.get(4+j)
                        + m.get(4*i + 2) * v.m.get(8+j)
                        + m.get(4*i + 3) * v.m.get(12+j));
            }
        }
        return r;
    }
    public static FloatBuffer getIndentityBuffer(){
        return identity;
    }
}