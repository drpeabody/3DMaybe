package EhNew.math;

/**
 * @since 28 Jan, 2016
 * @author Abhishek
 */
public class Vec2 {
    public float x, y;
    public Vec2(float a, float b){
        x = a;
        y = b;
    }
    public Vec2(){
        x = y = 0.0f;
    }

    @Override
    public String toString() {
        return "vec2: (" + x + ", " + y + ")";
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
        Vec2 o = (Vec2)v;
        return o.x == x && o.y == y;
    }
}