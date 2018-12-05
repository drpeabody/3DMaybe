package EhNew.geom;

import EhNew.math.Vec2;
import EhNew.math.Vec3;

/**
 * @author Abhishek
 */
public class Vertex {
    public static int SIZE = 12 + 8 + 12 + 12; //Number of Bytes of data per Vertex
    
    public static int OFFSET_POSITION = 0;
    public static int OFFSET_TEXTURE_COORD = 12;
    public static int OFFSET_NORMAL = 12 + 8;
    public static int OFFSET_TANGENT = 12 + 8 + 12;
    
    public static int POINTER_ATTRIB_POSITION = 0;
    public static int POINTER_ATTRIB_TEXTURE_COOD = 1;
    public static int POINTER_ATTRIB_NORMAL = 2;
    public static int POINTER_ATTRIB_TANGENT = 3;
    
    public Vec3 pos, normal, tangent;
    public Vec2 TextCoods;
    
    public Vertex(){
        pos = new Vec3();
        normal = new Vec3();
        TextCoods = new Vec2();
        tangent = new Vec3();
    }
        
    public static void calcTangents(Vertex[] v) {
        for (int i = 0; i < v.length; i += 3) {
            Vertex v0 = v[i];
            Vertex v1 = v[i + 1];
            Vertex v2 = v[i + 2];

            Vertex.calcTangents(v0, v1, v2);
        }
    }
    public static void calcTangents(Vertex[] v, int[] indeces, int elementsPerPrimitive) {
        if(elementsPerPrimitive == 3) {
            for (int i = 0; i < indeces.length; i += 3) {
                Vertex v0 = v[indeces[i]];
                Vertex v1 = v[indeces[i + 1]];
                Vertex v2 = v[indeces[i + 2]];

                Vertex.calcTangents(v0, v1, v2);
            }
        }
        else if(elementsPerPrimitive == 4){
            for (int i = 0; i < indeces.length; i += 4) {
                Vertex v0 = v[indeces[i]];
                Vertex v1 = v[indeces[i + 1]];
                Vertex v2 = v[indeces[i + 2]];
                Vertex v3 = v[indeces[i + 3]];

                Vertex.calcTangents(v0, v1, v2, v3);
            }
        }
    }
    public static void calcTangents(Vertex v0, Vertex v1, Vertex v2){
            Vec3 Edge1 = v1.pos.difference(v0.pos);
            Vec3 Edge2 = v2.pos.difference(v0.pos);

            float DeltaU1 = v1.TextCoods.x - v0.TextCoods.x;
            float DeltaV1 = v1.TextCoods.y - v0.TextCoods.y;
            float DeltaU2 = v2.TextCoods.x - v0.TextCoods.x;
            float DeltaV2 = v2.TextCoods.y - v0.TextCoods.y;

            float f = 1.0f / (DeltaU1 * DeltaV2 - DeltaU2 * DeltaV1);
            
            if(f == Float.NEGATIVE_INFINITY || f == Float.POSITIVE_INFINITY){
                f = 1.0f;
            }
            
            Vec3 Tangent = new Vec3(
                    (DeltaV2 * Edge1.x - DeltaV1 * Edge2.x),
                    (DeltaV2 * Edge1.y - DeltaV1 * Edge2.y),
                    (DeltaV2 * Edge1.z - DeltaV1 * Edge2.z)).product(f);

            v0.tangent.add(Tangent);
            v1.tangent.add(Tangent);
            v2.tangent.add(Tangent);
    }
    
    protected static void calcTangents(Vertex v0, Vertex v1, Vertex v2, Vertex v3) {
        Vec3 Edge1 = v1.pos.difference(v0.pos);
        Vec3 Edge2 = v3.pos.difference(v2.pos);

        float DeltaU1 = v1.TextCoods.x - v0.TextCoods.x;
        float DeltaV1 = v1.TextCoods.y - v0.TextCoods.y;
        float DeltaU2 = v3.TextCoods.x - v2.TextCoods.x;
        float DeltaV2 = v3.TextCoods.y - v2.TextCoods.y;

        float f = 1.0f / (DeltaU1 * DeltaV2 - DeltaU2 * DeltaV1);

        if (f == Float.NEGATIVE_INFINITY || f == Float.POSITIVE_INFINITY) {
            f = 1.0f;
        }

        Vec3 Tangent = new Vec3(
                (DeltaV2 * Edge1.x - DeltaV1 * Edge2.x),
                (DeltaV2 * Edge1.y - DeltaV1 * Edge2.y),
                (DeltaV2 * Edge1.z - DeltaV1 * Edge2.z)).product(f);

        v0.tangent.add(Tangent);
        v1.tangent.add(Tangent);
        v2.tangent.add(Tangent);
        v3.tangent.add(Tangent);
    }
    
    public static float[] getDataFrom(Vertex[] v){
        float vert[] = new float[v.length * Vertex.SIZE];
        int a = 0;
        for(Vertex x: v){
            vert[a++] = x.pos.x;vert[a++] = x.pos.y;vert[a++] = x.pos.z;
            vert[a++] = x.TextCoods.x;vert[a++] = x.TextCoods.y;
            vert[a++] = x.normal.x;vert[a++] = x.normal.y;vert[a++] = x.normal.z;
            vert[a++] = x.tangent.x;vert[a++] = x.tangent.y;vert[a++] = x.tangent.z;
        }
        return vert;
    }

    @Override
    public String toString() {
        return "Vertex: Pos: " + pos.toString() + " TCoord: " + TextCoods.toString() + 
                " Normal: " + normal.toString() + " Tangent: " + tangent.toString();
    }
}
