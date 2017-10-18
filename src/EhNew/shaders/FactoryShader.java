package EhNew.shaders;

import EhNew.data.DirectionalLight;
import EhNew.data.PointLight;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL20.*;

/**
 * @since 29 Jan, 2016
 * @author Abhishek
 */
public class FactoryShader extends Shader{
    public static final String UNIFORM_TRANSLATE_MATRIX = "trans";
    public static final String ATTRIB_INSTANCE_TRANSFORM_MATRIX = "instTransMat";
    public static final String UNIFORM_POJECTION_MATRIX = "proj";
    public static final String UNIFORM_CAM_MATRIX = "cam";
    public static final String UNIFORM_CAM_LOCATION = "WorldEyePos0";
    public static final String UNIFORM_DIRECTIONAL_LIGHT = "dirLight";
    public static final String UNIFORM_LIGHT_AMBIENT_STRENGTH = ".AmbiInten";
    public static final String UNIFORM_LIGHT_COLOR = ".light.color";
    public static final String UNIFORM_LIGHT_DIR = ".dir";
    public static final String UNIFORM_LIGHT_POSITION = ".pos";
    public static final String UNIFORM_LIGHT_DIFFUSE_STRENGTH = ".light.DiffInten";
    public static final String UNIFORM_LIGHT_SPECULAR_STRENGTH = ".light.MatSpecInten";
    public static final String UNIFORM_LIGHT_SPECULAR_POWER = ".light.SpecPower";
    public static final String UNIFORM_LIGHT_CUTOFF = ".cutOff";
    public static final String UNIFORM_LIGHT_FALLOFF = ".fallOff";
    public static final String UNIFORM_NUMBER_OF_POINTLIGHTS = "numLights";
    public static final String UNIFORM_POINTLIGHT_ARRAY = "lights";
    public static final String UNIFORM_TEXTURE_0 = "diffuseMap";
    public static final String UNIFORM_TEXTURE_1 = "normalMap";
    
    
    int numPointLights;
    
    int camMatLoc;
    int camLocLoc;

    public FactoryShader(){
        super();
        numPointLights = 0;
        camLocLoc = -1;
        camMatLoc = -1;
    }
    
    @Override
    public void loadShader(){
        addShader(compileShader(FactoryShader.class.getResourceAsStream("Shader_fragment.glsl"), GL_FRAGMENT_SHADER));
        addShader(compileShader(FactoryShader.class.getResourceAsStream("Shader_vertex.glsl"), GL_VERTEX_SHADER));
        glLinkProgram(programID);
        glUseProgram(programID);
        finalizeShader();
    }

    @Override
    public void finalizeShader() {
        super.finalizeShader();
        camMatLoc = glGetUniformLocation(programID, UNIFORM_CAM_MATRIX);
        camLocLoc = glGetUniformLocation(programID, UNIFORM_CAM_LOCATION);
        glUniform1i(glGetUniformLocation(programID, UNIFORM_TEXTURE_0), 0);
        glUniform1i(glGetUniformLocation(programID, UNIFORM_TEXTURE_1), 1);
    }
    
    public void updatePointLightProperty(PointLight l, String property){
        String light = UNIFORM_POINTLIGHT_ARRAY + "[" + l.getIdx() + "]";
        switch(property){
            case UNIFORM_LIGHT_COLOR:
                FloatBuffer f = BufferUtils.createFloatBuffer(3);
                f.put(l.color.x).put(l.color.y).put(l.color.z);
                f.flip();
                glUniform3fv(getUniformLocation(light + UNIFORM_LIGHT_COLOR), f);
                break;
            case UNIFORM_LIGHT_POSITION:
                f = BufferUtils.createFloatBuffer(3);
                f.put(l.pos.x).put(l.pos.y).put(l.pos.z);
                f.flip();
                glUniform3fv(getUniformLocation(light + UNIFORM_LIGHT_POSITION), f);
                break;
            case UNIFORM_LIGHT_DIFFUSE_STRENGTH:
                glUniform1f(getUniformLocation(light + UNIFORM_LIGHT_DIFFUSE_STRENGTH), l.diffInten);
                break;
            case UNIFORM_LIGHT_SPECULAR_POWER:
                glUniform1f(getUniformLocation(light + UNIFORM_LIGHT_SPECULAR_POWER), l.specPower);
                break;
            case UNIFORM_LIGHT_SPECULAR_STRENGTH:
                glUniform1f(getUniformLocation(light + UNIFORM_LIGHT_SPECULAR_STRENGTH), l.specInten);
                break;
            case UNIFORM_LIGHT_CUTOFF:
                glUniform1f(getUniformLocation(light + UNIFORM_LIGHT_CUTOFF), l.cutOff);
                break;
            case UNIFORM_LIGHT_FALLOFF:
                glUniform1f(getUniformLocation(light + UNIFORM_LIGHT_FALLOFF), l.fallOff);
        }
    }
    public void finalizePointLights(){
        glUniform1i(getUniformLocation(UNIFORM_NUMBER_OF_POINTLIGHTS), numPointLights);
    }
    public int addPointLight(PointLight l){
        String light = UNIFORM_POINTLIGHT_ARRAY + "[" + numPointLights + "]";
        glUniform1f(getUniformLocation(light + UNIFORM_LIGHT_DIFFUSE_STRENGTH), l.diffInten);
        glUniform1f(getUniformLocation(light + UNIFORM_LIGHT_SPECULAR_POWER), l.specPower);
        glUniform1f(getUniformLocation(light + UNIFORM_LIGHT_SPECULAR_STRENGTH), l.specInten);
        glUniform1f(getUniformLocation(light + UNIFORM_LIGHT_CUTOFF), l.cutOff);
        glUniform1f(getUniformLocation(light + UNIFORM_LIGHT_FALLOFF), l.fallOff);
        
        FloatBuffer f = BufferUtils.createFloatBuffer(3);
        f.put(l.color.x).put(l.color.y).put(l.color.z);
        f.flip();
        glUniform3fv(getUniformLocation(light + UNIFORM_LIGHT_COLOR), f);
        f = BufferUtils.createFloatBuffer(3);
        f.put(l.pos.x).put(l.pos.y).put(l.pos.z);
        f.flip();
        glUniform3fv(getUniformLocation(light + UNIFORM_LIGHT_POSITION), f);
        numPointLights++;
        return numPointLights -1;
    }
    public void updateDirectionalLighting(DirectionalLight l){
        l.dir.normalize();
        glUniform1f(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_AMBIENT_STRENGTH), l.ambInten);
        glUniform1f(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_DIFFUSE_STRENGTH), l.diffInten);
        glUniform1f(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_SPECULAR_POWER), l.specPower);
        FloatBuffer f = BufferUtils.createFloatBuffer(3);
        f.put(l.color.x).put(l.color.y).put(l.color.z);
        f.flip();
        glUniform3fv(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_COLOR), f);
        f = BufferUtils.createFloatBuffer(3);
        f.put(l.dir.x).put(l.dir.y).put(l.dir.z);
        f.flip();
        glUniform3fv(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_DIR), f);
    }
    public void updateAmbientLighting(DirectionalLight l){
        glUniform1f(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_AMBIENT_STRENGTH), l.ambInten);
        FloatBuffer f = BufferUtils.createFloatBuffer(3);
        f.put(l.color.x).put(l.color.y).put(l.color.z);
        f.flip();
        glUniform3fv(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_COLOR), f);
    }
    @Override
    public void updateProjection(float[] projMat){
        glUniformMatrix4fv(getUniformLocation(FactoryShader.UNIFORM_POJECTION_MATRIX),
                true, projMat);
    }
    @Override
    public void updateTransformation(float[] mat){
        glUniformMatrix4fv(getUniformLocation(FactoryShader.UNIFORM_TRANSLATE_MATRIX), 
                true, mat);
    }
    public void updateCameraMatrix(float[] camMat){
        glUniformMatrix4fv(camMatLoc, true, camMat);
    }
    public void updateCameraLocation(float[] camLoc){
        glUniform3fv(camLocLoc, camLoc);
    }

}