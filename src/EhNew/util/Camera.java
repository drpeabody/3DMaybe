package EhNew.util;

import EhNew.math.PerspectiveProjection;
import EhNew.math.Projection;
import EhNew.math.Vec2;
import EhNew.math.Vec3;
import EhNew.shaders.CamShader;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * @since 23 Jan, 2016
 * @author Abhishek
 */
public class Camera {
    private Projection projection;
    private float flySpeed, mouseSpeed;
    protected Vec3 loc, head, left, target;
    
    public int KeyForward = GLFW_KEY_W;
    public int KeyBackward = GLFW_KEY_S;
    public int KeyLeft = GLFW_KEY_A;
    public int KeyRight = GLFW_KEY_D;
    public int KeyUp = GLFW_KEY_SPACE;
    public int KeyDown = GLFW_KEY_LEFT_SHIFT;
    
    public boolean KeyForwardPressed = false;
    public boolean KeyBackwardPressed = false;
    public boolean KeyLeftPressed = false;
    public boolean KeyRightPressed = false;
    public boolean KeyUpPressed = false;
    public boolean KeyDownPressed = false;
    
    private boolean camMoved, camRotated;
    protected Vec3 dirMove;

    public Camera(){
        this(640,480,new Vec3(),new Vec3(0.0F, 1.0F, 0.0F),
                new Vec3(0.0F, 0.0F, 1.0F), 0.3F, 0.001F);
    }
    public Camera(float winW, float winH, Vec3 pos, Vec3 head, Vec3 target,
                float speed, float mouseSensitivity){
        if(winH < 1 || winW < 1 || pos == null || head == null || target == null
                || speed < 0 || mouseSensitivity < 0){
            throw new IllegalArgumentException("Bad Arguments to make Camera.");
        }
        this.head = head.unitVector();
        this.target = target.unitVector();
        left = this.head.cross(this.target);
        flySpeed = speed;
        mouseSpeed = mouseSensitivity;
        loc = new Vec3();
        camMoved = camRotated = true;
        dirMove = target.unitVector();
        projection = new PerspectiveProjection();
    }

    public float getFlySpeed() {
        return flySpeed;
    }
    public float getMouseSpeed() {
        return mouseSpeed;
    }
    public Vec3 getPos() {
        return loc;
    }
    public Vec3 getHead() {
        return head;
    }
    public Vec3 getLeft() {
        return left;
    }
    public Vec3 getTarget() {
        return target;
    }
    public Projection getProjection(){
        return projection;
    }

    public void setProjection(Projection projection) {
        this.projection = projection;
    }
    public void setCameraSpace(Vec3 head, Vec3 target, Vec3 left){
        this.head = head;
        this.target = target;
        this.left = left;
    }
    public void setFlySpeed(float flySpeed) {
        this.flySpeed = flySpeed;
    }
    public void setMouseSpeed(float mouseSpeed) {
        this.mouseSpeed = mouseSpeed;
    }
    public void setPos(Vec3 pos) {
        loc = pos;
    }
    public void setTarget(Vec3 target) {
        this.target = target.unitVector();
        left = this.head.cross(this.target).unitVector();
        //We need to normalise this vector due to inaccuracy caused by floating values
        //which tends to decrease modulus on every call
        head = this.target.cross(left).unitVector();
    }
    
    public void centreCam(){
        head = new Vec3(0.0f, 1.0f, 0.0f);
        left = head.cross(target).unitVector();
    }
    
    public void setCamRotated(boolean b){
        camRotated = b;
    }
    public void setCamMoved(boolean b){
        camMoved = b;
    }
    
    public void moveCameraBy(Vec3 displacement) {
        loc.add(displacement);
        camMoved = true;
    }
    public void rotateCameraBy(Vec2 mouseDisp) {
        float Hangle = -mouseDisp.x*mouseSpeed;
        float Vangle = mouseDisp.y*mouseSpeed;
        
        final Vec3 Vaxis = new Vec3(0f,1f,0f);
        
        float c = (float)Math.cos(Hangle);
        float s = (float)Math.sin(Hangle);
        float x = target.x * c - target.z * s;
        float z = target.x * s + target.z * c;
        target.x = x;
        target.z = z;
        
        left = Vaxis.cross(target).unitVector();
        target = target.rotateAbout(left, Vangle).unitVector();
        head = target.cross(left);
        camRotated = true;
    }
    
    public void updateCustomCameraMatrix(float mat[], CamShader f){
        f.updateCameraMatrix(mat);
    }
    
    public float[] calculatecameraMatrix(){
        return (new float[] {
            left.x,left.y, left.z, -loc.dot(left),
            head.x, head.y, head.z, -loc.dot(head),
            target.x, target.y, target.z, -loc.dot(target),
            0.0f, 0.0f, 0.0f, 1.0f});
    }
    
    public void update() {
        if (KeyBackwardPressed | KeyForwardPressed | KeyLeftPressed | KeyRightPressed | KeyUpPressed | KeyDownPressed) {
            moveCameraBy(dirMove.unitVector().product(flySpeed));
        }
    }

    public GLFWKeyCallback getKeyCallBack(){
        return new GLFWKeyCallback() {
            @Override
            public void invoke(long l, int key, int scancode, int action, int mods) {
                dirMove.x = dirMove.y = dirMove.z = 0f;
                if(action == GLFW_PRESS){
                    if(key == KeyForward) KeyForwardPressed = true;
                    if(key == KeyBackward) KeyBackwardPressed = true;
                    if(key == KeyLeft) KeyLeftPressed = true;
                    if(key == KeyRight) KeyRightPressed = true;
                    if(key == KeyDown) KeyDownPressed = true;
                    if(key == KeyUp) KeyUpPressed = true;
                }
                else if(action == GLFW_RELEASE){
                    if(key == KeyForward) KeyForwardPressed = false;
                    if(key == KeyBackward) KeyBackwardPressed = false;
                    if(key == KeyLeft) KeyLeftPressed = false;
                    if(key == KeyRight) KeyRightPressed = false;
                    if(key == KeyDown) KeyDownPressed = false;
                    if(key == KeyUp) KeyUpPressed = false;
                }
                if(KeyForwardPressed) dirMove.add(target.unitVector());
                else if(KeyBackwardPressed) dirMove.subtract(target.unitVector());
                if(KeyLeftPressed) dirMove.subtract(left.unitVector());
                else if(KeyRightPressed) dirMove.add(left.unitVector());
                if(KeyUpPressed) dirMove.add(new Vec3(0f,1f,0f));
                else if(KeyDownPressed) dirMove.subtract(new Vec3(0f, 1f, 0f));
            }
        };
    }
    public GLFWCursorPosCallback getMousePosCallback(){
        return new GLFWCursorPosCallback() {
                double preX = 0.0, preY = 0.0;
                @Override
                public void invoke(long window, double x, double y) {
                    rotateCameraBy(new Vec2((float) (x - preX), (float) (y - preY)));
                    camRotated = true;
                    preX = x;
                    preY = y;
                }
            };
    }
    
    public void updateCameraMatrices(CamShader f) {
        if (camMoved) {
            f.updateCameraLocation(getPos().getArray());
            f.updateCameraMatrix(calculatecameraMatrix());
        }
        else if (camRotated) {
            f.updateCameraMatrix(calculatecameraMatrix());
        }
        camMoved = false;
        camRotated = false;
    }
    public void updateProjection(CamShader f){
        f.updateProjection(projection.getProjectionMatrix());
    }
}
