package EhNew.math;

/**
 * @since Oct 27, 2017
 * @author Abhishek
 */
public class ProjectionTransform extends Transform{
    public float aspectRatio;
    public float width;
    public float fieldOfView;
    public float zNear;
    public float zFar;
    
    
    public ProjectionTransform(){
        super();
        aspectRatio = 1.0f;
        fieldOfView = 5*(float)(Math.PI/2);
        zNear = 1.0f;
        zFar = -1.0f;
    }
    
    public float getHeight(){
        return width/aspectRatio;
    }
    public float[] calculateProjection() {
        return (new float[] {
            (1.0f/(float)(Math.tan(fieldOfView/2)))/aspectRatio,0.0f,0.0f,0.0f,
            0.0f, 1.0f/(float)(Math.tan(fieldOfView/2)), 0.0f, 0.0f,
            0.0f, 0.0f, (-zNear -zFar)/(zNear - zFar), (2*zNear*zFar)/(zNear - zFar),
            0.0f, 0.0f, 1.0f, 0.0f});
    }

}
