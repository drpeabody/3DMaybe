package EhNew.math;

/**
 * @since 22 Jan, 2016
 * @author Abhishek
 */
public class Transformer {
    public float aspectRatio;
    public float width;
    public float fieldOfView;
    public float zNear;
    public float zFar;
    
    private Vec3 translation, rotation, scale;
    
    public Transformer(){
        aspectRatio = 1.0f;
        fieldOfView = 5*(float)(Math.PI/2);
        zNear = 1.0f;
        zFar = -1.0f;
        translation = new Vec3();
        rotation = new Vec3();
        scale = new Vec3(1.0f, 1.0f, 1.0f);
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
    
    public float getHeight(){
        return width/aspectRatio;
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
    public float[] calculateProjection() {
        return (new float[] {
            (1.0f/(float)(Math.tan(fieldOfView/2)))/aspectRatio,0.0f,0.0f,0.0f,
            0.0f, 1.0f/(float)(Math.tan(fieldOfView/2)), 0.0f, 0.0f,
            0.0f, 0.0f, (-zNear -zFar)/(zNear - zFar), (2*zNear*zFar)/(zNear - zFar),
            0.0f, 0.0f, 1.0f, 0.0f});
    }
}