package EhNew.physics;

import EhNew.math.Vec2;
import EhNew.math.Vec3;

/**
 * @since Jul 7, 2017
 * @author Abhishek
 */
public abstract class HitBox {

    protected int type;

    public static final int HITABOX_TYPE_AABB = 0;
    public static final int HITABOX_TYPE_SPHERE = 1;

    public abstract void translateBy(Vec3 dist);

    public abstract void rotateBy(Vec3 angle);

    public abstract void scaleBy(Vec3 scale);
    
    //It is assumed that the axis given is a unit vector.
    public abstract Vec2 getMinMaxProjAlongAxis(Vec3 axis);

    public static class OrientedBoundingBox extends HitBox {

        Vec3 start, end;

        public OrientedBoundingBox(Vec3 start, Vec3 end) {
            this.start = start;
            this.end = end;
            type = HITABOX_TYPE_AABB;
        }

        public OrientedBoundingBox(Vec3[] verts) {
            start = verts[0].sum(new Vec3());
            end = verts[0].sum(new Vec3());
            for (Vec3 v : verts) {
                if (v.x < start.x) {
                    start.x = v.x;
                }
                if (v.y < start.y) {
                    start.y = v.y;
                }
                if (v.z < start.z) {
                    start.z = v.z;
                }
                if (v.x > end.x) {
                    end.x = v.x;
                }
                if (v.y > end.y) {
                    end.y = v.y;
                }
                if (v.z > end.z) {
                    end.z = v.z;
                }
            }
            type = HITABOX_TYPE_AABB;
        }

        @Override
        public void translateBy(Vec3 dist) {
            start.add(dist);
            end.add(dist);
        }

        @Override
        public void rotateBy(Vec3 angle) {
            float cRx = (float) Math.cos(angle.x);
            float cRy = (float) Math.cos(angle.y);
            float cRz = (float) Math.cos(angle.z);
            float sRx = (float) Math.sin(angle.x);
            float sRy = (float) Math.sin(angle.y);
            float sRz = (float) Math.sin(angle.z);
            start.x = start.x * (cRy * cRz) + start.y * (-cRy * sRz) + start.z * (-sRy) + 0;
            start.y = start.x * (cRx * sRz - sRx * sRy * cRz) + start.y * (cRx * cRz + sRx * sRy * sRz) + start.z * (-sRx * cRy) + 0;
            start.z = start.x * (sRx * sRz + cRx * sRy * cRz) + start.y * (sRx * cRz - cRx * sRy * sRz) + start.z * (cRx * cRy) + 0;
            end.x = end.x * (cRy * cRz) + end.y * (-cRy * sRz) + end.z * (-sRy) + 0;
            end.y = end.x * (cRx * sRz - sRx * sRy * cRz) + end.y * (cRx * cRz + sRx * sRy * sRz) + end.z * (-sRx * cRy) + 0;
            end.z = end.x * (sRx * sRz + cRx * sRy * cRz) + end.y * (sRx * cRz - cRx * sRy * sRz) + end.z * (cRx * cRy) + 0;
        }

        @Override
        public void scaleBy(Vec3 scale) {
            start.multiply(scale);
            end.multiply(scale);
        }

        //Uses grey codes instead of looping for going over every vertexof the Box.
        //Since the function will be called a lot of times, it is imperitive to save time here.
        @Override
        public Vec2 getMinMaxProjAlongAxis(Vec3 axis) {
            float min = start.dot(axis), max = start.dot(axis), p;//0,0,0
            Vec3 v = new Vec3(start.x, start.y, end.z);//0,0,1
            p = v.x * axis.x + v.y * axis.y + v.z * axis.z;
            if(p < min) min = p;
            if(p > max) max = p;
            
            v.y = end.y;//0,1,1
            p = v.x * axis.x + v.y * axis.y + v.z * axis.z;
            if(p < min) min = p;
            if(p > max) max = p;
            
            v.z = start.z;//0,1,0
            p = v.x * axis.x + v.y * axis.y + v.z * axis.z;
            if(p < min) min = p;
            if(p > max) max = p;
            
            v.x = end.x;//1,1,0
            p = v.x * axis.x + v.y * axis.y + v.z * axis.z;
            if(p < min) min = p;
            if(p > max) max = p;
            
            v.y = start.y;//1,0,0
            p = v.x * axis.x + v.y * axis.y + v.z * axis.z;
            if(p < min) min = p;
            if(p > max) max = p;
            
            v.z = end.z;//1,0,1
            p = v.x * axis.x + v.y * axis.y + v.z * axis.z;
            if(p < min) min = p;
            if(p > max) max = p;
            
            v.y = end.y;//1,1,1
            p = v.x * axis.x + v.y * axis.y + v.z * axis.z;
            if(p < min) min = p;
            if(p > max) max = p;
            return new Vec2(min, max);
        }

    }

    public static class Sphere extends HitBox {
        //This factor converts the projection into a box whose area outside the
        //sphere is equal to the area of the sphere outside the box
        public static final float HalfSqRtPi = (float)Math.sqrt(Math.PI)/2;
        Vec3 pos;
        float radius;

        public Sphere(Vec3 pos, float r) {
            radius = r;
            this.pos = pos;
            type = HITABOX_TYPE_SPHERE;
        }

        @Override
        public void translateBy(Vec3 dist) {
            pos.add(dist);
        }

        @Override
        public void rotateBy(Vec3 angle) {
        }

        @Override
        public void scaleBy(Vec3 scale) {
            pos.multiply(scale);
        }

        @Override
        public Vec2 getMinMaxProjAlongAxis(Vec3 axis) {
            float d = pos.modulus();
            return new Vec2(d - radius*HalfSqRtPi, d + radius*HalfSqRtPi);
        }
    }
}
