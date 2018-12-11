package EhNew.util;

import EhNew.geom.Axes;
import EhNew.math.Vec3;

/**
 * @author Abhishek
 * @since 12 Dec, 2018
 */

public interface HitBox {
    //Implements Simple Axis-Aligned HitBox

    Vec3 getStart();
    Vec3 getEnd();

    default boolean isCollidingWith(HitBox b){
        if(getEnd().x >= b.getStart().x &&
                getEnd().y >= b.getStart().y &&
                getEnd().z >= b.getStart().z){
            return getStart().x <= b.getEnd().x &&
                    getStart().y <= b.getEnd().y &&
                    getStart().z <= b.getEnd().z;
        }
        return false;
    }

    //Debugging function
    default void draw(Axes ax){
        Vec3 t = ax.getTranslation();
        Vec3 r = ax.getRotation();
        Vec3 s = ax.getScale();

        Vec3 diff = getEnd().difference(getStart());
        ax.setTranslation(getStart());
        ax.setScale(diff);
        ax.draw();
        ax.translateBy(new Vec3(diff.x, diff.y, 0f));
        ax.setRotation(new Vec3(0f, 0f, (float)Math.PI));
        ax.draw();
        ax.translateBy(new Vec3(0f, -diff.y, diff.z));
        ax.setRotation(new Vec3((float)Math.PI, 0f, (float)Math.PI));
        ax.draw();
        ax.translateBy(new Vec3(-diff.x, diff.y, 0f));
        ax.setRotation(new Vec3((float)Math.PI, 0f, 0f));
        ax.draw();

        ax.setTranslation(t);
        ax.setRotation(r);
        ax.setScale(s);
    }
}
