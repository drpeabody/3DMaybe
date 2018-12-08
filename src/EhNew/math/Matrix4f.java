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

    public Vec4 applyToVector(Vec3 v){
        //Last Component is taken to be 0f
        return new Vec4(
            m[ 0] * v.x + m[ 1] * v.y + m[ 2] * v.z + m[ 3],
            m[ 4] * v.x + m[ 5] * v.y + m[ 6] * v.z + m[ 7],
            m[ 8] * v.x + m[ 9] * v.y + m[10] * v.z + m[11],
            m[12] * v.x + m[13] * v.y + m[14] * v.z + m[15]
        );
    }

    public Vec4 applyToVector(Vec4 v){
        return new Vec4(
            m[ 0] * v.r + m[ 1] * v.g + m[ 2] * v.b + m[ 3]* v.a,
            m[ 4] * v.r + m[ 5] * v.g + m[ 6] * v.b + m[ 7]* v.a,
            m[ 8] * v.r + m[ 9] * v.g + m[10] * v.b + m[11]* v.a,
            m[12] * v.r + m[13] * v.g + m[14] * v.b + m[15]* v.a
        );
    }

    public void transpose(){
        float k = m[1];
        m[1] = m[4];
        m[4] = k;
        k = m[2];
        m[2] = m[8];
        m[8] = k;
        k = m[3];
        m[3] = m[12];
        m[12] = k;
        k = m[6];
        m[6] = m[9];
        m[9] = k;
        k = m[7];
        m[7] = m[13];
        m[13] = k;
        k = m[11];
        m[11] = m[14];
        m[14] = k;
    }

}