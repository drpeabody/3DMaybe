package EhNew.math;


/**
 * @since 22 Jan, 2016
 * @author Abhishek
 */
public class Vec4 {
    //for quaternions, r + g(i) + b(j) + a(k)
    
    //0x 00 00 00 00
    //0x A  R  G  B
    public float r,g,b,a;
    public Vec4(float x, float y, float z, float w){
        r = x;
        g = y;
        b = z;
        a = w;
    }
    public Vec4(){}
    
    public Vec4 hamiltonProduct(Vec4 v){
        return new Vec4(
                r*v.r - g*v.g - b*v.b - a*v.a,
                r*v.g + g*v.r + b*v.a - a*v.b,
                r*v.b - g*v.a + b*v.r + a*v.g,
                r*v.a + g*v.b - b*v.g + a*v.r);
    }
    public Vec4 hamiltonVectorProduct(Vec3 v){
        return new Vec4(
                r*v.x - g*v.y - b*v.z ,
                r*v.y + g*v.x - a*v.z,
                r*v.z  + b*v.x + a*v.y,
                g*v.z - b*v.y + a*v.x);
    }
    public Vec4 hamiltonConjugate(){
        return new Vec4(r, -g, -b, -a);
    }

    @Override
    public String toString() {
        return "Vec4(" + r + ", " + g + ", " + b + ", " + a + ")";
    }
}