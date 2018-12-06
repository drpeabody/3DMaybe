package EhNew.shaders;

import org.lwjgl.opengl.GL13;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL20.*;

/**
 * @since 20 Oct, 2016
 * @author Abhishek
 */
public abstract class Shader {
    int programID;
    private int vertexShader, fragmentShader;
    
    public Shader(){
        programID = vertexShader = fragmentShader = -1;
    }
    
    public void init() {
        programID = glCreateProgram();
    }
    public void useShader() {
        if(programID == -1) throw new IllegalStateException("Shader not Initted.");
        glUseProgram(programID);
    }

    public void destroyShaders(){
        glUseProgram(0);
        glDeleteProgram(programID);
        programID = vertexShader = fragmentShader = -1;
    }
    
    int getUniformLocation(String s){
        return glGetUniformLocation(programID, s);
    }
    private static int compileShader(InputStream in, int type) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder program = new StringBuilder();
            String s;
            while ((s = br.readLine()) != null) {
                program.append(s);
                program.append("\n");
            }
            int sh = glCreateShader(type);
            glShaderSource(sh, program);
            glCompileShader(sh);
            s = glGetShaderInfoLog(sh);
            if (!s.isEmpty()) {
                System.out.println(s);
                System.exit(1);
            }
            return sh;
        } catch (Exception ignored) {}
        return -1;
    }

    void loadShader(String vertex, String fragment, Class c){
        fragmentShader = compileShader(c.getResourceAsStream(fragment), GL_FRAGMENT_SHADER);
        vertexShader = compileShader(c.getResourceAsStream(vertex), GL_VERTEX_SHADER);
        glAttachShader(programID, fragmentShader);
        glAttachShader(programID, vertexShader);
        glLinkProgram(programID);
        glUseProgram(programID);
        glDetachShader(programID, fragmentShader);
        glDetachShader(programID, vertexShader);
        glDeleteShader(fragmentShader);
        glDeleteShader(vertexShader);
    }

    public abstract void loadShader();
    public void updateTransformationVectors(float[] mat){}
    public void updateProjection(float[] mat){}
    //Since shader is handled by Engine, it assumes some defualt functionality.
    //If your shader doesn't use this functionality, leave the function empty.
    public int getDiffuseMapTextureUnit(){ return GL13.GL_TEXTURE0;}
    public int getNormalMapTextureUnit(){ return GL13.GL_TEXTURE0;}
    public int getEmmisiveMapTextureUnit(){ return GL13.GL_TEXTURE0;}
}