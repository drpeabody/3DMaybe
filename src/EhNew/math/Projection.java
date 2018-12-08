
package EhNew.math;

/**
 *
 * @author Abhishek
 */
public interface Projection {
    float[] getProjectionMatrix();
    PerspectiveProjection calculateProjection();
    Vec2 getScreenSizePixels();
    float getAspectRatio();
    float getFieldOfView();
    void setWidth(float f);
    void setAspectRatio(float f);
}
