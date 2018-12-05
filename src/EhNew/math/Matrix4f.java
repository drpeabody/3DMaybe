package EhNew.math;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

/**
 * @since 22 Jan, 2016
 * @author Abhishek
 */
public class Matrix4f {
    float[] m;
    public static final float identity[] = new float[]{
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
    };
    
    public Matrix4f(){
        m = new float[]{1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1};
    }
    public Matrix4f(Vec4 v1, Vec4 v2, Vec4 v3, Vec4 v4){
        m = new float[]{v1.r, v1.g, v1.b, v1.a, v2.r, v2.g, v2.b, v2.a, v3.r, v3.g, v3.b, v3.a, v4.r, v4.g, v4.b, v4.a};
    }
    public Matrix4f(float f[]){
        m = new float[16];
        System.arraycopy(f, 0, m, 0, (f.length > 16) ? 16 : f.length);
        for (int j = f.length; j < 16; j++) m[j] = 0f;
    }

    @Override
    public String toString() {
        return "Matrix4f (\n" + 
                m[ 0] + ",\t" + m[ 1] + ",\t" + m[ 2] + ",\t" + m[ 3] + ",\n" + 
                m[ 4] + ",\t" + m[ 5] + ",\t" + m[ 6] + ",\t" + m[ 7] + ",\n" + 
                m[ 8] + ",\t" + m[ 9] + ",\t" + m[10] + ",\t" + m[11] + ",\n" + 
                m[12] + ",\t" + m[13] + ",\t" + m[14] + ",\t" + m[15] + ")"; 
    }

}