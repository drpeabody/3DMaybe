package EhNew.util;

import java.util.ArrayList;

/**
 * @since Jul 1, 2017
 * @author Abhishek
 */
public class AnimUtil {
    public static final int ANIM_FUNC_LINEAR = 0;
    public static final int ANIM_FUNC_SINE = 1;
    public static final int ANIM_FUNC_SQUARE = 2;
    public static final double PIBY2 = Math.PI/2;
    
    ArrayList<Anim> AnimList;
    
    public AnimUtil(int defaultSize){
        AnimList = new ArrayList<>(defaultSize);
    }
    
    public float getAnimState(int idx){
        Anim a = AnimList.get(idx);
        long t = (System.nanoTime() - a.startTime) / a.lifeTime;
        switch(a.function){
            case ANIM_FUNC_LINEAR: return (float)Math.sin(PIBY2 * t);
            case ANIM_FUNC_SINE: return t;
            case ANIM_FUNC_SQUARE: return t * t;
            default: return 0.0f;
        }
    }
    
    public void registerAnim(int function, long time, long life){
        AnimList.add(new Anim(function, time, life));
    }
    public void registerAnimNow(int function, long life){
        AnimList.add(new Anim(function, System.nanoTime(), life));
    }
    
    public void optimiseList(){
        long t = System.nanoTime();
        for(int i = 0; i < AnimList.size(); i++){
            if(AnimList.get(i).startTime + AnimList.get(i).lifeTime < t){
                AnimList.remove(i);
            }
        }
    }
    
    public static class Anim{
        protected int function;
        protected long startTime;
        protected long lifeTime;

        public Anim(int function, long startTime, long lifeTime) {
            this.function = function;
            this.startTime = startTime;
            this.lifeTime = lifeTime;
        }
    }

}
