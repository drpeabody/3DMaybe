package EhNew.shaders;

import static EhNew.shaders.Shader.compileShader;
import org.lwjgl.opengl.GL13;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUseProgram;

/**
 * @since 27 Apr, 2017
 * @author Abhishek
 */
public class HUDShader extends Shader{
    public static final String UNIFORM_TEXTURE = "texture";
    
    public HUDShader(){
        super();
    }

    @Override
    public void loadShader() {
        addShader(compileShader(FactoryShader.class.getResourceAsStream("HUD_fragment.glsl"), GL_FRAGMENT_SHADER));
        addShader(compileShader(FactoryShader.class.getResourceAsStream("HUD_vertex.glsl"), GL_VERTEX_SHADER));
        glLinkProgram(programID);
        glUseProgram(programID);
        finalizeShader();
    }

    
    @Override
    public void updateTransformation(float[] mat) {
    }
    @Override
    public void finalizeShader() {
        super.finalizeShader();
        glUniform1i(glGetUniformLocation(programID, UNIFORM_TEXTURE), 0);
    }

    @Override
    public void updateProjection(float[] mat) {
        
    }

    @Override
    public int getDiffuseMapTextureUnit() {
        return GL13.GL_TEXTURE0;
    }

    @Override
    public int getNormalMapTextureUnit() {
        return GL13.GL_TEXTURE1;
    }

    @Override
    public int getInstanceTransformMapTextureUnit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
