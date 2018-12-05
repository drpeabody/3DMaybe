package EhNew.shaders;

import EhNew.data.DirectionalLight;
import EhNew.data.PointLight;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import static org.lwjgl.opengl.GL20.*;

/**
 * @since 29 Jan, 2016
 * @author Abhishek
 */
public class FactoryShader extends Shader implements CamShader{
    public final String UNIFORM_TRANSLATE_MATRIX = "trans";
    public final String UNIFORM_POJECTION_MATRIX = "proj";
    public final String UNIFORM_CAM_MATRIX = "cam";
    public final String UNIFORM_CAM_LOCATION = "WorldEyePos0";
    public final String UNIFORM_DIRECTIONAL_LIGHT = "dirLight";
    public final String UNIFORM_LIGHT_AMBIENT_STRENGTH = ".AmbiInten";
    public final String UNIFORM_LIGHT_COLOR = ".light.color";
    public final String UNIFORM_LIGHT_DIR = ".dir";
    public final String UNIFORM_LIGHT_POSITION = ".pos";
    public final String UNIFORM_LIGHT_DIFFUSE_STRENGTH = ".light.DiffInten";
    public final String UNIFORM_LIGHT_SPECULAR_STRENGTH = ".light.MatSpecInten";
    public final String UNIFORM_LIGHT_SPECULAR_POWER = ".light.SpecPower";
    public final String UNIFORM_LIGHT_CUTOFF = ".cutOff";
    public final String UNIFORM_LIGHT_FALLOFF = ".fallOff";
    public final String UNIFORM_NUMBER_OF_POINTLIGHTS = "numLights";
    public final String UNIFORM_POINTLIGHT_ARRAY = "lights";
    public final String UNIFORM_TEXTURE_DIFFUSEMAP = "diffuseMap";
    public final String UNIFORM_TEXTURE_NORMALMAP = "normalMap";
    public final String UNIFORM_TEXTURE_EMMISIVEMAP = "emmisiveMap";
    public final String UNIFORM_TEXTURE_INSTANCE_TRANSFORM_MAP = "instTransMatrices";
    public final String UNIFORM_INSTANCE_TRANSFORM_MAP_SIZE = "instTransMapSize";
    
    public final int TEXTURE_UNIT_DIFFURE_MAP = 0;
    public final int TEXTURE_UNIT_NORMAL_MAP = 1;
    public final int TEXTURE_UNIT_EMMISIVE_MAP = 2;
    
    private int numPointLights;
    
    private int camMatLoc;
    private int camLocLoc;

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
        glUniform1i(glGetUniformLocation(programID, UNIFORM_TEXTURE_DIFFUSEMAP), TEXTURE_UNIT_DIFFURE_MAP);
        glUniform1i(glGetUniformLocation(programID, UNIFORM_TEXTURE_NORMALMAP), TEXTURE_UNIT_NORMAL_MAP);
        glUniform1i(glGetUniformLocation(programID, UNIFORM_TEXTURE_EMMISIVEMAP), TEXTURE_UNIT_EMMISIVE_MAP);
    }
    
    public void updatePointLightProperty(PointLight l, String property){
        String light = UNIFORM_POINTLIGHT_ARRAY + "[" + l.getIdx() + "]";
        float f[];
        switch(property){
            case UNIFORM_LIGHT_COLOR:
                f = new float[] {l.color.x, l.color.y, l.color.z };
                glUniform3fv(getUniformLocation(light + UNIFORM_LIGHT_COLOR), f);
                break;
            case UNIFORM_LIGHT_POSITION:
                f = new float[] {l.pos.x, l.pos.y, l.pos.z };
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

        float f[] = new float[] {l.color.x, l.color.y, l.color.z };
        glUniform3fv(getUniformLocation(light + UNIFORM_LIGHT_COLOR), f);

        float g[] = new float[] {l.pos.x, l.pos.y, l.pos.z };
        glUniform3fv(getUniformLocation(light + UNIFORM_LIGHT_POSITION), g);
        numPointLights++;
        return numPointLights -1;
    }
    public void updateDirectionalLighting(DirectionalLight l){
        l.dir.normalize();
        glUniform1f(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_AMBIENT_STRENGTH), l.ambInten);
        glUniform1f(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_DIFFUSE_STRENGTH), l.diffInten);
        glUniform1f(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_SPECULAR_POWER), l.specPower);

        float f[] = new float[] {l.color.x, l.color.y, l.color.z };
        glUniform3fv(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_COLOR), f);

        float g[] = new float[] {l.dir.x, l.dir.y, l.dir.z };
        glUniform3fv(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_DIR), g);
    }
    public void updateAmbientLighting(DirectionalLight l){
        glUniform1f(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_AMBIENT_STRENGTH), l.ambInten);
        float f[] = new float[] {l.color.x, l.color.y, l.color.z };
        glUniform3fv(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_COLOR), f);
    }
    @Override
    public void updateProjection(float[] projMat){
        glUniformMatrix4fv(getUniformLocation(UNIFORM_POJECTION_MATRIX),
                true, projMat);
    }
    @Override
    public void updateTransformationVectors(float[] mat){
        glUniformMatrix4fv(getUniformLocation(UNIFORM_TRANSLATE_MATRIX), 
                true, mat);
    }
    @Override
    public void updateCameraMatrix(float[] camMat){
        glUniformMatrix4fv(camMatLoc, true, camMat);
    }
    @Override
    public void updateCameraLocation(float[] camLoc){
        glUniform3fv(camLocLoc, camLoc);
    }
    
    
    @Override
    public int getDiffuseMapTextureUnit(){
        return GL13.GL_TEXTURE0;
    }
    @Override
    public int getNormalMapTextureUnit(){
        return GL13.GL_TEXTURE1;
    }
    @Override
    public int getEmmisiveMapTextureUnit(){
        return GL13.GL_TEXTURE2;
    }
}
