package EhNew.util.HUD;

import EhNew.math.*;
import EhNew.util.Camera;

import java.awt.image.BufferedImage;

/**
 * @author Abhishek
 * @since 07 Dec, 2018
 */

public class HUDTarget extends PictureBox {

    Vec3 pos; //The World Space target that will be tracked
    Camera c;

    public HUDTarget(Vec3 pos, Vec2 size, BufferedImage text, Vec4 color, Camera c) {
        super(new Vec2(), size, text, color);
        this.pos = pos;
        this.c = c;
    }

    public void update(){

        Projection p = c.getProjection();
        float ar = p.getAspectRatio();
        float t = (float)(Math.tan(p.getFieldOfView()/2));
        Vec3 camPos = pos.difference(c.getPos()).unitVector();
        Vec2 scrPos = new Vec2(c.getLeft().dot(camPos)/(-ar*t), c.getHead().dot(camPos)/t);

//        Matrix4f proj = new Matrix4f(p.getProjectionMatrix());
//        Matrix4f cam = new Matrix4f(c.calculatecameraMatrix());
//        Vec4 s = proj.applyToVector(cam.applyToVector(new Vec4(0f, 0f, 0f, 1f)));
//        float f = c.getPos().modulus();
//        Vec2 scrPos = new Vec2(s.r/f, s.g/f);

        movePositionTo(scrPos);
        //We cannot update buffer here as this is the updater thread and there is
        //no OpenGL context present here.
        //Also movePositionTo() automatically sets a flag
        //That updates the buffer the next time this HUD Element is drawn.

//        updateBuffer();

    }
}
