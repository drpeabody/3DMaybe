package EhNew.geom;

import EhNew.DrawableEntity;
import EhNew.math.Vec2;
import EhNew.math.Vec3;
import EhNew.shaders.CamShader;
import EhNew.shaders.Shader;
import EhNew.util.Camera;
import org.lwjgl.opengl.GL11;

/**
 * @since Apr 14, 2018
 * @author Abhishek
 */
public abstract class Sprite extends DrawableEntity{
    private Camera c;

    public <T extends Shader & CamShader>Sprite(T s, Camera c) {
        super(s);
        this.c = c;
    }

    @Override
    public void load() {
        drawMode = GL11.GL_TRIANGLES;
        Vertex v[] = new Vertex[]{new Vertex(), new Vertex(), new Vertex(), new Vertex()};
        int arr[] = new int[]{0,1,2,2,3,0};

        v[0].TextCoods = new Vec2(0f, 0f);
        v[1].TextCoods = new Vec2(1f, 0f);
        v[2].TextCoods = new Vec2(1f, 1f);
        v[3].TextCoods = new Vec2(0f, 1f);

        v[0].normal = new Vec3(0f, 0f, 1f);
        v[1].normal = new Vec3(0f, 0f, 1f);
        v[2].normal = new Vec3(0f, 0f, 1f);
        v[3].normal = new Vec3(0f, 0f, 1f);

        v[0].tangent = new Vec3(0f, 1f, 0f);
        v[1].tangent = new Vec3(0f, 1f, 0f);
        v[2].tangent = new Vec3(0f, 1f, 0f);
        v[3].tangent = new Vec3(0f, 1f, 0f);

        v[0].pos = new Vec3(-1f, -1f, 0f);
        v[1].pos = new Vec3(1f, -1f, 0f);
        v[2].pos = new Vec3(1f, 1f, 0f);
        v[3].pos = new Vec3(-1f, 1f, 0f);

        load(Vertex.getDataFrom(v), arr);
    }

    public CamShader getCamChader(){
        return (CamShader)s;
    }

    //Texture binding needs to be done manually. Then just call this draw function.
    //Do not forget to unbind your textures.
    @Override
    public void draw() {
        Vec3 X = c.getPos().difference(translation);//(0, 0, 1)
        rotation.y = (float)(-Math.atan2(X.x, X.z));
        rotation.z = 0f;
        rotation.x = (float)(-Math.atan2(X.y, Math.sqrt(X.x*X.x + X.z*X.z)));
        //Setting this rotation correctly results in the bill-boarding effect for Sprites.

        super.draw();
    }

    public void draw(Axes ax){
        Vec3 X = c.getPos().difference(translation);//(0, 0, 1)

        rotation.y = (float)(-Math.atan2(X.x, X.z));
        rotation.z = 0f;
        rotation.x = (float)(-Math.atan2(X.y, Math.sqrt(X.x*X.x + X.z*X.z)));

        Vec3 t = ax.getTranslation();
        Vec3 r = ax.getRotation();

        ax.setTranslation(translation);
        ax.setRotation(rotation);
        ax.draw();

        ax.setTranslation(t);
        ax.setRotation(r);
        super.draw();
    }
}
