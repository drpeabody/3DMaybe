package EhNew.geom;

import EhNew.DrawableEntity;
import EhNew.math.Vec3;
import EhNew.shaders.FactoryShader;
import EhNew.shaders.Shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;

/**
 * @since 6 Dec, 2018
 * @author Abhishek
 */
public class Axes extends DrawableEntity {

    public Axes(Shader s) {
        super(s);
    }

    @Override
    public void load() {
        Vertex v[] = new Vertex[]{
                new Vertex(), new Vertex(), new Vertex(), new Vertex()
        };

        v[0].pos = new Vec3();
        v[1].pos = new Vec3(1f, 0f, 0f);
        v[2].pos = new Vec3(0f, 1f, 0f);
        v[3].pos = new Vec3(0f, 0f, 1f);

        int arr[] = new int[]{
                0, 1, 0, 2, 0, 3
        };

        load(Vertex.getDataFrom(v), arr);
    }

    @Override
    public void loadDataFrom(DrawableEntity d) {
        if(! (d instanceof Axes) )
            throw new IllegalArgumentException("Cannot Load Axes) from Class");
        load(d.getShader(), d.getVertID(), d.getIdxID(), d.getIndexCount(),
                d.getIndexOffset(), d.getDrawMode());
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

        ((FactoryShader)s).updateEmmisiveColor(0f, 0f, 1f, 1f);
        glDrawElements(GL_LINES, 2, GL_UNSIGNED_INT, 16);

        ((FactoryShader)s).updateEmmisiveColor(0f, 1f, 0f, 1f);
        glDrawElements(GL_LINES, 2, GL_UNSIGNED_INT, 8);

        ((FactoryShader)s).updateEmmisiveColor(1f, 0f, 0f, 1f);
        glDrawElements(GL_LINES, 2, GL_UNSIGNED_INT, 0);

        ((FactoryShader)s).updateEmmisiveColor(0f, 0f, 0f, 0f);
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_POSITION);
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_TEXTURE_COOD);
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_NORMAL);
        glDisableVertexAttribArray(Vertex.POINTER_ATTRIB_TANGENT);
    }
}
