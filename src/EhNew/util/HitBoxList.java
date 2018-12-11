package EhNew.util;

import EhNew.geom.Axes;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Abhishek
 * @since 12 Dec, 2018
 */

public class HitBoxList {

    CopyOnWriteArrayList<HitBox> hitlist;

    public HitBoxList(){
        hitlist = new CopyOnWriteArrayList<>();
    }

    public void add(HitBox h){
        hitlist.add(h);
    }
    public void remove(HitBox h){
        hitlist.remove(h);
    }

    public void forCollisionWith(HitBox ht, HitBoxConsumer hc){
        for(HitBox h: hitlist){
            if(ht.isCollidingWith(h)) {
                hc.consume(h);
                break;
            }
        }
    }
    public void forCollisionWith(HitBoxList hbl, HitResolver hc){
        for(HitBox h: hitlist){
            for(HitBox it: hbl.hitlist) {
                if (it.isCollidingWith(h)) {
                    hc.consume(h, it);
                    break;
                }
            }
        }
    }

    public void draw(Axes ax){
        for(HitBox h: hitlist){
            h.draw(ax);
        }
    }

    public static interface HitBoxConsumer{
        public void consume(HitBox h);
    }

    public static interface HitResolver{
        public void consume(HitBox h1, HitBox h2);
    }
}
