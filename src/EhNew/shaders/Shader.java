package EhNew.shaders;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL20.*;

/**
 * @since 20 Oct, 2016
 * @author Abhishek
 */
public abstract class Shader {
    protected int programID;
    protected ArrayList<Integer> shaders;
    
    public Shader(){
        programID = -1;
        shaders = new ArrayList<>();
    }
    
    public void init() {
        programID = glCreateProgram();
    }
    public void useShader() {
        if(programID == -1) throw new IllegalStateException("Shader not Initted.");
        glUseProgram(programID);
    }
    public void addShader(int shader) {
        if(programID == -1) throw new IllegalStateException("Shader not Initted.");
        if (!glIsShader(shader)) throw new IllegalArgumentException("Invalid Shader.");
        glAttachShader(programID, shader);
        shaders.add(shader);
    }
    public void destroyShaders(){
        shaders.clear();
        glUseProgram(0);
        glDeleteProgram(programID);
        programID = -1;
    }
    
    public int getUniformLocation(String s){
        return glGetUniformLocation(programID, s);
    }
    public int getAttribLocation(String s){
        return glGetAttribLocation(programID, s);
    }
    public void finalizeShader() {
        if(programID == -1) throw new IllegalStateException("Processor not Initted.");
        shaders.stream().map((i) -> {
            glDetachShader(programID, i);
            return i;
        }).forEach((i) -> {
            glDeleteShader(i);
        });
    }
    
    public static int compileShader(InputStream in, int type) {
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
            }
            return sh;
        } catch (Exception e) {}
        return -1;
    }
    
    public abstract void loadShader();
    public abstract void updateTransformation(float[] mat);
    public abstract void updateProjection(float[] mat);
    //Since shader ishandled by Engine, it assumes some defualt functionality.
    //If your shader doesn't use this functionality, leave the function empty.
}