package EhNew.physics;

import EhNew.DrawableEntity;
import EhNew.geom.Vertex;
import EhNew.math.Vec2;
import EhNew.math.Vec3;
import EhNew.shaders.Shader;
import EhNew.util.OBJLoader;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

/**
 * @since Jul 6, 2017
 * @author Abhishek
 */
public abstract class PhysicsEntity extends DrawableEntity{
    HitBox hitBox;
    float mass;
    
    public PhysicsEntity(Shader s){
        super(s);
    }

    @Override
    public void translateBy(Vec3 v) {
        super.translateBy(v);
        hitBox.translateBy(v);
    }
    @Override
    public void rotateBy(Vec3 v) {
        super.rotateBy(v);
        hitBox.translateBy(v);
    }
    @Override
    public void scaleBy(Vec3 v){
        super.scaleBy(v);
        hitBox.scaleBy(scale);
    }
    
    @Override
    public void load(InputStream s) {
        if (idxID != -1 || vertID != -1) {
            throw new IllegalStateException("Attempting to create pre-created Entity");
        }
        OBJLoader.Mesh e = OBJLoader.loadMesh((s));
        Vertex v[] = e.getVertexData();
        int i[] = e.getIndexData();
        
        Vec3 start = v[0].pos.sum(new Vec3());
        Vec3 end = v[0].pos.sum(new Vec3());
        for (Vertex r : v) {
            if (r.pos.x < start.x) start.x = r.pos.x;
            if (r.pos.y < start.y) start.y = r.pos.y;
            if (r.pos.z < start.z) start.z = r.pos.z;
            if (r.pos.x > end.x) end.x = r.pos.x;
            if (r.pos.y > end.y) end.y = r.pos.y;
            if (r.pos.z > end.z) end.z = r.pos.z;
        }
        hitBox = new HitBox.OrientedBoundingBox(start, end);

        Vertex.calcTangents(v, i, 3);
        
        IntBuffer idx = Vertex.getDataFrom(i);
        FloatBuffer vert = Vertex.getDataFrom(v);
        indexCount = idx.limit();
                
        if(indexOffset < 0) {
            idx.flip();
            vert.flip();
            indexOffset = 0;
            idxID = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxID);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, idx, GL_STATIC_DRAW);

            vertID = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vertID);
            glBufferData(GL_ARRAY_BUFFER, vert, GL_STATIC_DRAW);
        }
    }
   
    //Update to account for Super fast mostion
    public static boolean areColliding(HitBox hb1, HitBox hb2){
//        if(hb1.type == HitBox.HITABOX_TYPE_SPHERE && hb2.type == HitBox.HITABOX_TYPE_SPHERE) {
//            HitBox.Sphere s = (HitBox.Sphere)hb1;
//            HitBox.Sphere r = (HitBox.Sphere)hb2;
//            float d = s.radius + r.radius;
//            d = d * d;
//            return d < (s.pos.x - r.pos.x) * (s.pos.x - r.pos.x) + (s.pos.y - r.pos.y) * (s.pos.y - r.pos.y) + (s.pos.z - r.pos.z) * (s.pos.z - r.pos.z);
//        }
//        else if(hb1.type == HitBox.HITABOX_TYPE_AABB && hb2.type == HitBox.HITABOX_TYPE_AABB){
//            HitBox.OrientedBoundingBox s = (HitBox.OrientedBoundingBox)hb1;
//            HitBox.OrientedBoundingBox r = (HitBox.OrientedBoundingBox)hb2;
//            if(s.start.x <= r.end.x && s.start.y <= r.end.y && s.start.z <= r.end.z){
//               if(s.end.x >= r.start.x && s.end.y >= r.start.y && s.end.z >= r.start.z){
//                   return true;
//               } 
//            }
//        }
//        else if(hb1.type == HitBox.HITABOX_TYPE_AABB && hb2.type == HitBox.HITABOX_TYPE_SPHERE){
//            HitBox.OrientedBoundingBox s = (HitBox.OrientedBoundingBox)hb1;
//            HitBox.Sphere r = (HitBox.Sphere)hb2;
//            float dmin = 0f;
//            
//            if (r.pos.x < s.start.x) {
//                dmin += (r.pos.x - s.start.x)*(r.pos.x - s.start.x);
//            } else if (r.pos.x > s.end.x) {
//                dmin += (r.pos.x - s.end.x)*(r.pos.x - s.end.x);
//            }
//
//            if (r.pos.y < s.start.y) {
//                dmin += (r.pos.y - s.start.y)*(r.pos.y - s.start.y);
//            } else if (r.pos.y > s.end.y) {
//                dmin += (r.pos.y - s.end.y)*(r.pos.y - s.end.y);
//            }
//
//            if (r.pos.z < s.start.z) {
//                dmin += (r.pos.z - s.start.z)*(r.pos.z - s.start.z);
//            } else if (r.pos.z > s.end.z) {
//                dmin += (r.pos.z - s.end.z)*(r.pos.z - s.end.z);
//            }
//            return dmin <= r.radius*r.radius;
//        }
        Vec3 axis = new Vec3(1f,0f,0f);
        Vec2 p1 = hb1.getMinMaxProjAlongAxis(axis), p2 = hb2.getMinMaxProjAlongAxis(axis);
        if(p1.y < p2.x || p1.x > p2.y) return false;
        axis.x--;
        axis.y++;
        p1 = hb1.getMinMaxProjAlongAxis(axis); p2 = hb2.getMinMaxProjAlongAxis(axis);
        if(p1.y < p2.x || p1.x > p2.y) return false;
        axis.y--;
        axis.z++;
        p1 = hb1.getMinMaxProjAlongAxis(axis); p2 = hb2.getMinMaxProjAlongAxis(axis);
        return p1.y > p2.x || p1.x < p2.y;
    }
}
