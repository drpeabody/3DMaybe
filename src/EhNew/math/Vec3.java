package EhNew.math;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

/**
 * @since 22 Jan, 2016
 * @author Abhishek
 */
public class Vec3 {
    public float x,y,z;
    public Vec3(float a, float b, float c){
        x = a;
        y = b;
        z = c;
    }
    public Vec3() {
        x=y=z=0.0f;
    }
    
    public Vec3 sum(Vec3 v){
       return new Vec3(x+v.x, y+v.y, z+v.z);
    }
    public Vec3 difference(Vec3 v){
       return new Vec3(x-v.x, y-v.y, z-v.z);
    }
    public Vec3 product(float f){
        return new Vec3(x*f, y*f, z*f);
    }
    public Vec3 unitVector(){
        float f = modulus();
        return new Vec3(x/f, y/f, z/f);
    }
    public Vec3 negative(){
        return new Vec3(-x, -y, -z);
    }
    
    public void add(Vec3 v){
        x += v.x;
        y += v.y;
        z += v.z;
    }
    public void subtract(Vec3 v){
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }
    public void multiply(Vec3 v){
        x *= v.x;
        y *= v.y;
        z *= v.z;
    }
    public void multiply(float f){
        x *= f;
        y *= f;
        z *= f;
    }
    public void normalize(){
        float f = modulus();
        x = x/f;
        y = y/f;
        z = z/f;
    }
    public void negate(){
        x = -x;
        y = -y;
        z = -z;
    }
    
    public float dot(Vec3 v){
        return v.x*x + v.y*y + v.z*z;
    }
    public float modulus(){
        return (float)(Math.sqrt(x*x + y*y + z*z));
    }
    public float sqMod(){
        return x*x + y*y + z*z;
    }
    public Vec3 cross(Vec3 v){
        Vec3 f = new Vec3();
        f.x = y*v.z - v.y*z;
        f.y = v.x*z - x*v.z;
        f.z = x*v.y - v.x*y;
        return f;
    }
    public Vec3 rotateAbout(Vec3 r, float angle){
        r.normalize();
        Vec3 v = r.product((1 - (float)Math.cos(angle))*(dot(r)));
        v.add(product((float)(Math.cos(angle))));
        v.add(r.cross(this).product(((float)(Math.sin(angle)))));
        return v;
    }
    
    public void rotateAboutAxes(Vec3 angle){
        float cRx = (float) Math.cos(angle.x);
        float cRy = (float) Math.cos(angle.y);
        float cRz = (float) Math.cos(angle.z);
        float sRx = (float) Math.sin(angle.x);
        float sRy = (float) Math.sin(angle.y);
        float sRz = (float) Math.sin(angle.z);
        float a = x;
        float b = y;
        float c = z;
        x = ((cRy*cRz) * a) +               ((-cRy*sRz) * b) +              ((-sRy) * c);
        y = ((cRx*sRz - sRx*sRy*cRz) * a) + ((cRx*cRz + sRx*sRy*sRz) * b) + ((-sRx*cRy) * c);
        z = ((sRx*sRz + cRx*sRy*cRz) * a) + ((sRx*cRz - cRx*sRy*sRz) * b) + ((cRx*cRy) * c);
    }
    public void counterRotateAboutAxes(Vec3 angle){
        //Uses the fact the Counter rotation matrix is inverse or totation, which is
        //transpose of rotation.
        float cRx = (float) Math.cos(angle.x);
        float cRy = (float) Math.cos(angle.y);
        float cRz = (float) Math.cos(angle.z);
        float sRx = (float) Math.sin(angle.x);
        float sRy = (float) Math.sin(angle.y);
        float sRz = (float) Math.sin(angle.z);
        float a = x;
        float b = y;
        float c = z;
        x = ((cRy*cRz) * a)  + ((cRx*sRz - sRx*sRy*cRz) * b) + ((sRx*sRz + cRx*sRy*cRz) * c);
        y = ((-cRy*sRz) * a) + ((cRx*cRz + sRx*sRy*sRz) * b) + ((sRx*cRz - cRx*sRy*sRz) * c);
        z = ((-sRy) * a)     + ((-sRx*cRy) * b)              + ((cRx*cRy) * c);
    }
    
    @Override
    public String toString(){
        return "Vec3: (" + x + ", " + y + ", " + z + ")";
    }
    
    public float[] getArray(){
        return new float[]{x, y, z};
    }
    
    public FloatBuffer getBuffer(){
        FloatBuffer f = BufferUtils.createFloatBuffer(3);
        f.put(x).put(y).put(z).flip();
        return f;
    }
    
    @Override
    public boolean equals(Object v) {
        if (this == v) {
            return true;
        }
        if (v == null) {
            return false;
        }
        if (getClass() != v.getClass()) {
            return false;
        }
        Vec3 o = (Vec3)v;
        return o.x == x && o.y == y && o.z == z;
    }
}