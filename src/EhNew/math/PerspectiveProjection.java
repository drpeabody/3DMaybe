package EhNew.math;

/**
 * @since Oct 27, 2017
 * @author Abhishek
 */
public class PerspectiveProjection implements Projection{
    private float aspectRatio;
    private float width;
    private float fieldOfView;
    private float zNear;
    private float zFar;
    
    private float[] projectionMatrix;
    
    public PerspectiveProjection(){
        super();
        aspectRatio = 1.0f;
        fieldOfView = 5*(float)(Math.PI/2);
        zNear = 1.0f;
        zFar = -1.0f;
        projectionMatrix = null;
    }
    
    @Override
    public float[] getProjectionMatrix(){
        if(projectionMatrix == null){
            return Matrix4f.identity;
        }
        else{
            return projectionMatrix;
        }
    }
    
    public float getHeight(){
        return width/aspectRatio;
    }

    public float getWidth() {
        return width;
    }

    @Override
    public PerspectiveProjection calculateProjection() {
        projectionMatrix = (new float[] {
            (1.0f/(float)(Math.tan(fieldOfView/2)))/aspectRatio,0.0f,0.0f,0.0f,
            0.0f, 1.0f/(float)(Math.tan(fieldOfView/2)), 0.0f, 0.0f,
            0.0f, 0.0f, (-zNear -zFar)/(zNear - zFar), (2*zNear*zFar)/(zNear - zFar),
            0.0f, 0.0f, 1.0f, 0.0f});
        return this;
    }

    @Override
    public Vec2 getScreenSizePixels() {
        return new Vec2(width, width / aspectRatio);
    }

    @Override
    public void setWidth(float f) {
        width = f;
    }

    @Override
    public void setAspectRatio(float f) {
        aspectRatio = f;
    }

}
