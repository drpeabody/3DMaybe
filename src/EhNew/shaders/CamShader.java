/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EhNew.shaders;

/**
 *
 * @author Abhishek
 */
public interface CamShader{
    
    public void updateCameraMatrix(float[] camMat);
    public void updateCameraLocation(float[] camLoc);
}
