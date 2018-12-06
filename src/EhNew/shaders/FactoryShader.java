package EhNew.shaders;

import EhNew.data.DirectionalLight;
import EhNew.data.PointLight;
import EhNew.math.Vec3;
import EhNew.math.Vec4;
import org.lwjgl.opengl.GL13;
import static org.lwjgl.opengl.GL20.*;

/**
 * @since 29 Jan, 2016
 * @author Abhishek
 */
public class FactoryShader extends Shader implements CamShader{
    public final String UNIFORM_TRANSLATE_MATRIX = "trans",
        UNIFORM_POJECTION_MATRIX = "proj",
        UNIFORM_CAM_MATRIX = "cam",
        UNIFORM_CAM_LOCATION = "WorldEyePos0",
        UNIFORM_DIRECTIONAL_LIGHT = "dirLight",
        UNIFORM_LIGHT_AMBIENT_STRENGTH = ".AmbiInten",
        UNIFORM_LIGHT_COLOR = ".light.color",
        UNIFORM_LIGHT_DIR = ".dir",
        UNIFORM_LIGHT_POSITION = ".pos",
        UNIFORM_LIGHT_DIFFUSE_STRENGTH = ".light.DiffInten",
        UNIFORM_LIGHT_SPECULAR_STRENGTH = ".light.MatSpecInten",
        UNIFORM_LIGHT_SPECULAR_POWER = ".light.SpecPower",
        UNIFORM_LIGHT_CUTOFF = ".cutOff",
        UNIFORM_LIGHT_FALLOFF = ".fallOff",
        UNIFORM_NUMBER_OF_POINTLIGHTS = "numLights",
        UNIFORM_POINTLIGHT_ARRAY = "lights",
        UNIFORM_TEXTURE_DIFFUSEMAP = "diffuseMap",
        UNIFORM_TEXTURE_NORMALMAP = "normalMap",
        UNIFORM_TEXTURE_EMMISIVEMAP = "emmisiveMap",
        UNIFORM_DIFFUSECOLOR = "diffuseColor",
        UNIFORM_EMMISIVECOLOR = "emmisiveColor";

    public final int TEXTURE_UNIT_DIFFURE_MAP = 0,
        TEXTURE_UNIT_NORMAL_MAP = 1,
        TEXTURE_UNIT_EMMISIVE_MAP = 2;
    
    private int numPointLights;
    private int camMatLoc, camLocLoc;

    public FactoryShader(){
        super();
        numPointLights = 0;
        camLocLoc = camMatLoc = -1;
    }
    
    @Override
    public void loadShader(){
        loadShader("Shader_vertex.glsl", "Shader_fragment.glsl", FactoryShader.class);
        camMatLoc = getUniformLocation(UNIFORM_CAM_MATRIX);
        camLocLoc = getUniformLocation(UNIFORM_CAM_LOCATION);
        glUniform1i(getUniformLocation(UNIFORM_TEXTURE_DIFFUSEMAP), TEXTURE_UNIT_DIFFURE_MAP);
        glUniform1i(getUniformLocation(UNIFORM_TEXTURE_NORMALMAP), TEXTURE_UNIT_NORMAL_MAP);
        glUniform1i(getUniformLocation(UNIFORM_TEXTURE_EMMISIVEMAP), TEXTURE_UNIT_EMMISIVE_MAP);
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
    public void updateEmmisiveColor(float r, float g, float b, float a){
        glUniform4fv(getUniformLocation(UNIFORM_EMMISIVECOLOR), new float[]{r,g,b,a});
    }
    public void updateAmbientLighting(DirectionalLight l){
        glUniform1f(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_AMBIENT_STRENGTH), l.ambInten);
        float f[] = new float[] {l.color.x, l.color.y, l.color.z };
        glUniform3fv(getUniformLocation(UNIFORM_DIRECTIONAL_LIGHT + UNIFORM_LIGHT_COLOR), f);
    }
    @Override
    public void updateProjection(float[] projMat){ glUniformMatrix4fv(getUniformLocation(UNIFORM_POJECTION_MATRIX), true, projMat);  }
    @Override
    public void updateTransformationVectors(float[] mat){ glUniformMatrix4fv(getUniformLocation(UNIFORM_TRANSLATE_MATRIX), true, mat);  }
    @Override
    public void updateCameraMatrix(float[] camMat){ glUniformMatrix4fv(camMatLoc, true, camMat); }
    @Override
    public void updateCameraLocation(float[] camLoc){ glUniform3fv(camLocLoc, camLoc); }
    @Override
    public int getDiffuseMapTextureUnit(){ return GL13.GL_TEXTURE0; }
    @Override
    public int getNormalMapTextureUnit(){ return GL13.GL_TEXTURE1; }
    @Override
    public int getEmmisiveMapTextureUnit(){ return GL13.GL_TEXTURE2; }

}
