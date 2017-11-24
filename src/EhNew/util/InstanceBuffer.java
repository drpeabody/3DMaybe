package EhNew.util;

import EhNew.DrawableEntity;
import EhNew.Engine;
import EhNew.math.Matrix4f;
import static org.lwjgl.opengl.GL11.*;
import EhNew.math.Transform;
import EhNew.math.Vec3;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

/**
 * @since Oct 27, 2017
 * @author Abhishek
 */
public class InstanceBuffer {
    //Makes a texture out of a list of instances
    //Holds universal transformation data
    //draw() - to bind th etexture and draw, then bind the default texture again
    
    DrawableEntity model;//Model used to replicate instances in instanced arrays
    
    int transformTexture = -1;//Contains information about the transformation of instances. 
            //Refers to an OpenGL texture
    
    Engine r;
    //Holds a static 4 pixel Texture of a unit matrix when instanced rednering is not in use.
    
    public InstanceBuffer(DrawableEntity en, Engine e){
        if(en == null) throw new IllegalArgumentException("Model to be instanced cannot be null");
        model = en;
        r = e;
    }
    
    
    public void load(int textureUnit, Matrix4f[] trans){
        //Load the Texture for instancing
        if(transformTexture != -1)
            throw new IllegalStateException("Attemptin gto load preloaded Instace Data");
        
        GL13.glActiveTexture(textureUnit);
        transformTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_1D, transformTexture);
        glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        
        FloatBuffer b = BufferUtils.createFloatBuffer(trans.length * 16);
        for (Matrix4f f: trans) b.put(f.getArray());
        b.flip();
        
        glTexImage1D(GL_TEXTURE_1D, 0, GL30.GL_RGBA32F, trans.length * 4, 0, GL_RGBA, GL_FLOAT, b);
    }
    
    public static class Instance{
        //Holds transform data for every instance
        Transform trans;
        
        public Instance(Vec3 pos, Vec3 rot, Vec3 scale){
            trans = new Transform(pos, rot, scale);
        }
        
        public float[] getData(){
            return trans.calculateTransformation();
        }
        
    }
}
