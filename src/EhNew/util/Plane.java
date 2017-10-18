package EhNew.util;

import EhNew.math.Vec3;

/**
 * @since 14 Oct, 2016
 * @author Abhishek
 */
public class Plane {
    public Vec3 a, normal;
    
    public Plane(Vec3 p, Vec3 normal){
        a = p;
        this.normal = normal;
        if(this.normal.dot(a) < 0) this.normal.negate(); //Keeps the normal pointed away from the origin for all defined planse.
        this.normal.normalize();
    }
    public Plane(Vec3 a, Vec3 b, Vec3 c){
        this.a = a;
        normal = b.difference(c).cross(b.difference(a));
        if(normal.dot(a) < 0) normal.negate();
        normal.normalize();
    }
    
    public float distanceFrom(Vec3 p){
        return Math.abs(p.difference(a).dot(normal));
    }
    
    public boolean areOnTheSameSide(Vec3 p, Vec3 q){
        return a.difference(p).dot(normal) * a.difference(q).dot(normal) > 0;
    }
}