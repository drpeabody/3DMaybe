package EhNew.math;

/**
 * @since 22 Jan, 2016
 * @author Abhishek
 */
public class Transform {
    private final Vec3 translation, rotation, scale;
    
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
    
    public void scaleTo(Vec3 s){
        scale.x = s.x;
        scale.y = s.y;
        scale.z = s.z;
    }
    public void rotateTo(Vec3 s) {
        rotation.x = s.x;
        rotation.y = s.y;
        rotation.z = s.z;
    }
    public void translateTo(Vec3 s){
        translation.x = s.x;
        translation.y = s.y;
        translation.z = s.z;
    }
    public void scaleBy(Vec3 s){
        scale.x *= s.x;
        scale.y *= s.y;
        scale.z *= s.z;
    }
    public void rotateBy(Vec3 s) {
        rotation.x += s.x;
        rotation.y += s.y;
        rotation.z += s.z;
    }
    public void translateBy(Vec3 s){
        translation.x += s.x;
        translation.y += s.y;
        translation.z += s.z;
    }
    
    public float[] calculateTranslationMatrix(){
        return new float[]{
          1f, 0f, 0f, translation.x,
          0f, 1f, 0f, translation.y,
          0f, 0f, 1f, translation.z,
          0f, 0f, 0f, 1f,
        };
    }
    public float[] calculateRotationMatrix(){
        float cRx = (float)Math.cos(rotation.x);
        float cRy = (float)Math.cos(rotation.y);
        float cRz = (float)Math.cos(rotation.z);
        float sRx = (float)Math.sin(rotation.x);
        float sRy = (float)Math.sin(rotation.y);
        float sRz = (float)Math.sin(rotation.z);
        return new float[]{
            (cRy*cRz),               (-cRy*sRz),              (-sRy),     0f,
            (cRx*sRz - sRx*sRy*cRz), (cRx*cRz + sRx*sRy*sRz), (-sRx*cRy), 0f,
            (sRx*sRz + cRx*sRy*cRz), (sRx*cRz - cRx*sRy*sRz), (cRx*cRy),  0f,
            0.0f, 0.0f, 0.0f, 1.0f
        };
    }
    public float[] calculateScaleMatrix(){
        return new float[]{
            scale.x, 0f, 0f, 0f,
            0f, scale.y, 0f, 0f,
            0f, 0f, scale.z, 0f,
            0f, 0f, 0f, 1f
        };
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
    
    
    public Vec3 getTranslation() {
        return translation;
    }
    public Vec3 getScale() {
        return scale;
    }
    public Vec3 getRotation() {
        return rotation;
    }
}