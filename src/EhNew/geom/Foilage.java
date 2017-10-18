package EhNew.geom;

import EhNew.DrawableEntity;
import EhNew.util.Texture;
import java.awt.image.BufferedImage;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import org.lwjgl.opengl.GL13;

/**
 * @since Jul 12, 2017
 * @author Abhishek
 */
public class Foilage extends DrawableEntity{
    Texture diffuse, normal;
    
    public Foilage(BufferedImage diff, BufferedImage norm){
        diffuse = new Texture(GL_TEXTURE_2D, GL13.GL_TEXTURE0, diff);
        normal = new Texture(GL_TEXTURE_2D, GL13.GL_TEXTURE1, norm);
    }

    @Override
    public void load() {
        throw new IllegalStateException("Foilage is not self loadable");
    }

    public Texture getDiffuseMap(){
        return diffuse;
    }
    public Texture getNormalMap(){
        return normal;
    }
    public Vertex[] getVertices(){
        Vertex[] q0 = new Vertex[]{new Vertex(), new Vertex(), new Vertex(), new Vertex()};
        Vertex[] q1 = new Vertex[]{new Vertex(), new Vertex(), new Vertex(), new Vertex()};
        Vertex[] q2 = new Vertex[]{new Vertex(), new Vertex(), new Vertex(), new Vertex()};
        Vertex[] q3 = new Vertex[]{new Vertex(), new Vertex(), new Vertex(), new Vertex()};
        float d = (float)(1/Math.sqrt(2));
        q0[0].pos.x = q0[0].pos.z = -d;
        q0[1].pos.x = q0[1].pos.z = d;
        q0[2].pos.x = q0[2].pos.z = d; q0[2].pos.y = 1f;
        q0[3].pos.x = q0[3].pos.z = -d; q0[3].pos.y = 1f;
        q0[0].normal.x = d;
        q0[0].normal.z = -d;
        q0[1].normal = q0[2].normal = q0[3].normal = q0[0].normal;
        q0[1].TextCoods.x = q0[2].TextCoods.x = q0[2].TextCoods.y = q0[3].TextCoods.y = 1f;
        q0[0].tangent.x = q0[0].tangent.z = d;
        q0[1].tangent = q0[2].tangent = q0[3].tangent = q0[0].tangent;
        
        q1[0].pos.x = q1[0].pos.z = -d;
        q1[1].pos.x = q1[1].pos.z = d;
        q1[2].pos.x = q1[2].pos.z = d; q1[2].pos.y = 1f;
        q1[3].pos.x = q1[3].pos.z = -d; q1[3].pos.y = 1f;
        q1[0].normal.x = -d;
        q1[0].normal.z = d;
        q1[1].normal = q1[2].normal = q1[3].normal = q1[0].normal;
        q1[1].TextCoods.x = q1[2].TextCoods.x = q1[2].TextCoods.y = q1[3].TextCoods.y = 1f;
        q1[0].tangent.x = q1[0].tangent.z = d;
        q1[1].tangent = q1[2].tangent = q1[3].tangent = q1[0].tangent;
        
        q2[0].pos.x = q2[0].pos.z = d;
        q2[1].pos.x = q2[1].pos.z = -d;
        q2[2].pos.x = q2[2].pos.z = -d; q2[2].pos.y = 1f;
        q2[3].pos.x = q2[3].pos.z = d; q2[3].pos.y = 1f;
        q2[0].normal.x = -d;
        q2[0].normal.z = -d;
        q2[1].normal = q2[2].normal = q2[3].normal = q2[0].normal;
        q2[1].TextCoods.x = q2[2].TextCoods.x = q2[2].TextCoods.y = q2[3].TextCoods.y = 1f;
        q2[0].tangent.x = q2[0].tangent.z = d;
        q2[1].tangent = q2[2].tangent = q2[3].tangent = q2[0].tangent;
        
        q3[0].pos.x = q3[0].pos.z = d;
        q3[1].pos.x = q3[1].pos.z = -d;
        q3[2].pos.x = q3[2].pos.z = -d; q3[2].pos.y = 1f;
        q3[3].pos.x = q3[3].pos.z = d; q3[3].pos.y = 1f;
        q3[0].normal.x = d;
        q3[0].normal.z = d;
        q3[1].normal = q3[2].normal = q3[3].normal = q3[0].normal;
        q3[1].TextCoods.x = q3[2].TextCoods.x = q3[2].TextCoods.y = q3[3].TextCoods.y = 1f;
        q3[0].tangent.x = q3[0].tangent.z = d;
        q3[1].tangent = q3[2].tangent = q3[3].tangent = q3[0].tangent;
        return new Vertex[]{q0[0], q0[1], q0[2], q0[3],
                            q1[0], q1[1], q1[2], q1[3],
                            q2[0], q2[1], q2[2], q2[3],
                            q3[0], q3[1], q3[2], q3[3]};
    }
    
    @Override
    public void draw() {
        throw new IllegalStateException("Foilage is not self drawable");
    }
    
    public int getFloatsPerInstanceVertData(){
        return 16 * Vertex.SIZE / 4;//Default Mesh for Foilage has 16 vertices so the result needs to be divided in order to get number of floats instead of bytes
    }
}
