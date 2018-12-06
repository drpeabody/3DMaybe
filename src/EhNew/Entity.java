package EhNew;

import EhNew.math.Vec3;

/**
 * @since 22 Jan, 2016
 * @author Abhishek
 */
public abstract class Entity {
    protected  Vec3 translation, scale, rotation;
    
    public abstract void load();
    public abstract void draw();
    public abstract void destroy();
    
    Entity(){
        rotation = new Vec3();
        scale = new Vec3(1.0f, 1.0f, 1.0f);
        translation = new Vec3();
    }
    
    public void translateBy(Vec3 v){
        translation.add(v);
    }
    public void rotateBy(Vec3 v){
        rotation.add(v);
    }
    public void scaleBy(Vec3 v){
        scale.add(v);
    }

    public void setTranslation(Vec3 translation) {
        this.translation = translation;
    }
    public void setScale(Vec3 scale) {
        this.scale = scale;
    }
    public void setRotation(Vec3 rotation) {
        this.rotation = rotation;
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