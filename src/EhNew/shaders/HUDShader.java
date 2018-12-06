package EhNew.shaders;

import org.lwjgl.opengl.GL13;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

/**
 * @since 27 Apr, 2017
 * @author Abhishek
 */
public class HUDShader extends Shader{
    public static final String UNIFORM_TEXTURE = "texture";

    @Override
    public void loadShader() {
        loadShader("HUD_vertex.glsl", "HUD_fragment.glsl", HUDShader.class);
        glUniform1i(glGetUniformLocation(programID, UNIFORM_TEXTURE), 0);
    }

    @Override
    public int getDiffuseMapTextureUnit() { return GL13.GL_TEXTURE0; }
    @Override
    public int getNormalMapTextureUnit() { return GL13.GL_TEXTURE1; }
}
