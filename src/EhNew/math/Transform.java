package EhNew.math;

/**
 * @since 22 Jan, 2016
 * @author Abhishek
 */
public class Transform {
    private Vec3 translation, rotation, scale;
    
    public Transform(){
        translation = new Vec3();
        rotation = new Vec3();
        scale = new Vec3(1.0f, 1.0f, 1.0f);
    }
    public Transform(Vec3 pos, Vec3 rot, Vec3 scal){
        translation = pos;
        rotation = rot;
        scale = scal;
    }
    
    public void scale(Vec3 s){
        scale = s;
    }
    public void rotate(Vec3 s) {
        rotation = s;
    }
    public void translate(Vec3 s){
        translation = s;
    }
    
    public float[] calculateTransformation(){
        float cRx = (float)Math.cos(rotation.x);
        float cRy = (float)Math.cos(rotation.y);
        float cRz = (float)Math.cos(rotation.z);
        float sRx = (float)Math.sin(rotation.x);
        float sRy = (float)Math.sin(rotation.y);
        float sRz = (float)Math.sin(rotation.z);
//        //This 4x4 matrix is translation * roaionX * rotaionY * rotationZ * scale
        float[] f = new float[]{
            scale.x*(cRy*cRz),               scale.y*(-cRy*sRz),              scale.z*(-sRy),     translation.x,
            
            scale.x*(cRx*sRz - sRx*sRy*cRz), scale.y*(cRx*cRz + sRx*sRy*sRz), scale.z*(-sRx*cRy), translation.y,
            
            scale.x*(sRx*sRz + cRx*sRy*cRz), scale.y*(sRx*cRz - cRx*sRy*sRz), scale.z*(cRx*cRy),  translation.z,
            
            0.0f, 0.0f, 0.0f, 1.0f
        };
        
        return f;
    }
}