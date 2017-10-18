package EhNew;

import EhNew.math.Vec3;

/**
 * @since 22 Jan, 2016
 * @author Abhishek
 */
public abstract class Entity {
    protected  Vec3 translation, scale, rotation;
    
    public abstract void load();
    public abstract void draw(int drawMode);
    public abstract void draw();
    public abstract void destroy();
    
    protected Entity(){
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