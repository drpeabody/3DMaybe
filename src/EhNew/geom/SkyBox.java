package EhNew.geom;

import EhNew.DrawableEntity;
import EhNew.Engine;
import EhNew.shaders.Shader;
import EhNew.util.Texture;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

/**
 * @author Abhishek
 * @since 07 Dec, 2018
 */

// Uses 6 Emmissive maps to form a SkyBox
public class SkyBox extends DrawableEntity {

    Texture xPos, xNeg, yPos, yNeg, zPos, zNeg;
    private float scale;

    public SkyBox(Shader s, float scale, BufferedImage image){
        super(s);
        if(image == null){
            try {
                image = ImageIO.read(Engine.class.getResourceAsStream("nullCubeMap.jpg"));
            } catch (IOException ignored) {}
        }

        int w = image.getWidth()/4, h = image.getHeight()/3;
        //Represent 1x1 textures over the cubemap

        zNeg = new Texture(GL_TEXTURE_2D, s.getEmmisiveMapTextureUnit(),
                image.getSubimage(0, h, w, h));
        xPos = new Texture(GL_TEXTURE_2D, s.getEmmisiveMapTextureUnit(),
                image.getSubimage(w, h, w, h));
        zPos = new Texture(GL_TEXTURE_2D, s.getEmmisiveMapTextureUnit(),
                image.getSubimage(2*w, h, w, h));
        xNeg = new Texture(GL_TEXTURE_2D, s.getEmmisiveMapTextureUnit(),
                image.getSubimage(3*w, h, w, h));
        yPos = new Texture(GL_TEXTURE_2D, s.getEmmisiveMapTextureUnit(),
                image.getSubimage(w, 0, w, h));
        yNeg = new Texture(GL_TEXTURE_2D, s.getEmmisiveMapTextureUnit(),
                image.getSubimage(w, 2*h, w, h));
        this.scale = scale;
    }

    @Override
    public void load() {
        zNeg.loadFromImage();
        zNeg.bufferData();
        xPos.loadFromImage();
        xPos.bufferData();
        zPos.loadFromImage();
        zPos.bufferData();
        xNeg.loadFromImage();
        xNeg.bufferData();
        yPos.loadFromImage();
        yPos.bufferData();
        yNeg.loadFromImage();
        yNeg.bufferData();

        drawMode = GL_QUADS;

        Vertex v[] = new Vertex[]{
                new Vertex(), new Vertex(), new Vertex(), new Vertex(), //X+
                new Vertex(), new Vertex(), new Vertex(), new Vertex(), //X-
                new Vertex(), new Vertex(), new Vertex(), new Vertex(), //Z+
                new Vertex(), new Vertex(), new Vertex(), new Vertex(), //Z-
                new Vertex(), new Vertex(), new Vertex(), new Vertex(), //Y+
                new Vertex(), new Vertex(), new Vertex(), new Vertex()  //Y-
        };

        v[0].pos.x = v[1].pos.x = v[2].pos.x = v[3].pos.x = 1f*scale;
        v[0].pos.y = v[1].pos.y = 1f*scale;
        v[2].pos.y = v[3].pos.y = -1f*scale;
        v[0].pos.z = v[3].pos.z = 1f*scale;
        v[2].pos.z = v[1].pos.z = -1f*scale;

        v[0].TextCoods.y = v[1].TextCoods.y = 0f;
        v[2].TextCoods.y = v[3].TextCoods.y = 1f;
        v[0].TextCoods.x = v[3].TextCoods.x = 0f;
        v[2].TextCoods.x = v[1].TextCoods.x = 1f;

        v[4].pos.x = v[5].pos.x = v[6].pos.x = v[7].pos.x = -1f*scale;
        v[4].pos.y = v[5].pos.y = 1f*scale;
        v[6].pos.y = v[7].pos.y = -1f*scale;
        v[4].pos.z = v[7].pos.z = -1f*scale;
        v[6].pos.z = v[5].pos.z = 1f*scale;

        v[4].TextCoods.y = v[5].TextCoods.y = 0f;
        v[6].TextCoods.y = v[7].TextCoods.y = 1f;
        v[4].TextCoods.x = v[7].TextCoods.x = 0f;
        v[6].TextCoods.x = v[5].TextCoods.x = 1f;

        v[8].pos.z = v[9].pos.z = v[10].pos.z = v[11].pos.z = -1f*scale;
        v[8].pos.y = v[9].pos.y = 1f*scale;
        v[10].pos.y = v[11].pos.y = -1f*scale;
        v[8].pos.x = v[11].pos.x = 1f*scale;
        v[10].pos.x = v[9].pos.x = -1f*scale;

        v[8].TextCoods.y = v[9].TextCoods.y = 0f;
        v[10].TextCoods.y = v[11].TextCoods.y = 1f;
        v[8].TextCoods.x = v[11].TextCoods.x = 0f;
        v[10].TextCoods.x = v[9].TextCoods.x = 1f;

        v[12].pos.z = v[13].pos.z = v[14].pos.z = v[15].pos.z = +1f*scale;
        v[12].pos.y = v[13].pos.y = 1f*scale;
        v[14].pos.y = v[15].pos.y = -1f*scale;
        v[12].pos.x = v[15].pos.x = -1f*scale;
        v[14].pos.x = v[13].pos.x = 1f*scale;

        v[12].TextCoods.y = v[13].TextCoods.y = 0f;
        v[14].TextCoods.y = v[15].TextCoods.y = 1f;
        v[12].TextCoods.x = v[15].TextCoods.x = 0f;
        v[14].TextCoods.x = v[13].TextCoods.x = 1f;

        v[16].pos.y = v[17].pos.y = v[18].pos.y = v[19].pos.y = -1f*scale;
        v[16].pos.z = v[17].pos.z = -1f*scale;
        v[18].pos.z = v[19].pos.z = 1f*scale;
        v[16].pos.x = v[19].pos.x = 1f*scale;
        v[18].pos.x = v[17].pos.x = -1f*scale;

        v[18].TextCoods.y = v[17].TextCoods.y = 1f;
        v[16].TextCoods.y = v[19].TextCoods.y = 0f;
        v[18].TextCoods.x = v[19].TextCoods.x = 0f;
        v[16].TextCoods.x = v[17].TextCoods.x = 1f;

        v[20].pos.y = v[21].pos.y = v[22].pos.y = v[23].pos.y = 1f*scale;
        v[20].pos.z = v[21].pos.z = -1f*scale;
        v[22].pos.z = v[23].pos.z = 1f*scale;
        v[20].pos.x = v[23].pos.x = -1f*scale;
        v[22].pos.x = v[21].pos.x = 1f*scale;

        v[22].TextCoods.y = v[21].TextCoods.y = 1f;
        v[20].TextCoods.y = v[23].TextCoods.y = 0f;
        v[22].TextCoods.x = v[23].TextCoods.x = 0f;
        v[20].TextCoods.x = v[21].TextCoods.x = 1f;

        int arr[] = new int[]{
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
                16, 17, 18, 19, 20, 21, 22, 23
        };

        load(Vertex.getDataFrom(v), arr);
    }

    @Override
    public void draw() {
        s.updateTransformationVectors(calculateTransformation());

        glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_POSITION);
        glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_TEXTURE_COOD);
        glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_NORMAL);
        glEnableVertexAttribArray(Vertex.POINTER_ATTRIB_TANGENT);

        glBindBuffer(GL_ARRAY_BUFFER, getVertID());
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, getIdxID());

        glVertexAttribPointer(Vertex.POINTER_ATTRIB_POSITION, 3, GL_FLOAT, false, Vertex.SIZE, Vertex.OFFSET_POSITION);
        glVertexAttribPointer(Vertex.POINTER_ATTRIB_TEXTURE_COOD, 2, GL_FLOAT, false, Vertex.SIZE, Vertex.OFFSET_TEXTURE_COORD);
        glVertexAttribPointer(Vertex.POINTER_ATTRIB_NORMAL, 3, GL_FLOAT, false, Vertex.SIZE, Vertex.OFFSET_NORMAL);
        glVertexAttribPointer(Vertex.POINTER_ATTRIB_TANGENT, 3, GL_FLOAT, false, Vertex.SIZE, Vertex.OFFSET_TANGENT);

        xPos.bind();
        glDrawElements(drawMode, 4, GL_UNSIGNED_INT, 0);
        xNeg.bind();
        glDrawElements(drawMode, 4, GL_UNSIGNED_INT, 4*4);
        zPos.bind();
        glDrawElements(drawMode, 4, GL_UNSIGNED_INT, 8*4);
        zNeg.bind();
        glDrawElements(drawMode, 4, GL_UNSIGNED_INT, 12*4);
        yNeg.bind();
        glDrawElements(drawMode, 4, GL_UNSIGNED_INT, 16*4);
        yPos.bind();
        glDrawElements(drawMode, 4, GL_UNSIGNED_INT, 20*4);

        yPos.unBind();
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_POSITION);
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_TEXTURE_COOD);
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_NORMAL);
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_TANGENT);
    }
}
