package EhNew.util;

import EhNew.math.Vec3;
import EhNew.math.Vec2;
import EhNew.geom.Vertex;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Abhishek
 */
public class OBJLoader {
    
    public static Mesh loadMesh(InputStream f) {
        Mesh m = new Mesh();
        
        ArrayList<Vec3> verts = new ArrayList<>();
        ArrayList<Vec3> norms = new ArrayList<>();
        ArrayList<Vec2> coords = new ArrayList<>();
        ArrayList<String> verDat = new ArrayList<>();
        String line;

        try(BufferedReader br = new BufferedReader(new InputStreamReader(f))) {
                
                while ((line = br.readLine()) != null) {
                    String[] tokens = line.split(" ");
                    
                    if (tokens[0].equals("vt")) {
                        coords.add(new Vec2(Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2])));
                    } else if (tokens[0].equals("v")) {
                        verts.add(new Vec3(Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]),
                                Float.parseFloat(tokens[3])));
                    } else if (tokens[0].equals("vn")) {
                        norms.add(new Vec3(Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]),
                                Float.parseFloat(tokens[3])).negative());
                    } else if (tokens[0].startsWith("f")) {
                        verDat.add(tokens[1]);
                        verDat.add(tokens[2]);
                        verDat.add(tokens[3]);
                    }
                }
            
        } catch(FileNotFoundException ex){
            return null;
        } catch(IOException ex){
            return null;
        }
        int idx = 0;
        for (int i = 0; i < verDat.size(); i++) {
            String v1[] = verDat.get(i).split("/");
            Vertex V1 = new Vertex();
            V1.pos = verts.get(Integer.parseInt(v1[0]) - 1);
            V1.TextCoods = coords.get(Integer.parseInt(v1[1]) - 1);
            V1.normal = norms.get(Integer.parseInt(v1[2]) - 1);
            boolean found = false;
            for (int j = 0; j < i && !found; j++) {
                if (verDat.get(j).equals(verDat.get(i))) {
                    for (int k = 0; k < m.indices.size(); k++) {
                        if(m.data.get(m.indices.get(k)).equals(V1)){
                            m.indices.add(m.indices.get(k));
                            found = true;
                            break;
                        }
                    }
                }
            }
            if (!found) {
                m.indices.add(idx++);
                m.data.add(V1);
            }
//                m.indices.add(m.indices.size());
//                m.data.add(V1);
        }
        
        return m;
    }     
    public static class Mesh {
        ArrayList<Vertex> data;
        ArrayList<Integer> indices;
        
        public Mesh(){
            data = new ArrayList<>();
            indices = new ArrayList<>();
        }
        
        public Vertex[] getVertexData(){
            return data.toArray(new Vertex[data.size()]);
        }
        public int[] getIndexData(){
            Integer id[] = indices.toArray(new Integer[indices.size()]);
            int i[] = new int[id.length];
            for (int j = 0; j < i.length; j++) {
                i[j] = id[j];
            }
            return i;
            }
    }
}