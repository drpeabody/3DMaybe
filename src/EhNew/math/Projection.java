
package EhNew.math;

/**
 *
 * @author Abhishek
 */
public interface Projection {
    public float[] getProjectionMatrix();
    public PerspectiveProjection calculateProjection();
    public Vec2 getScreenSizePixels();
    public void setWidth(float f);
    public void setAspectRatio(float f);
    
}
