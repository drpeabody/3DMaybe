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
    void updateCameraMatrix(float[] camMat);
    void updateCameraLocation(float[] camLoc);
    void updateProjection(float[] projMat);
}
