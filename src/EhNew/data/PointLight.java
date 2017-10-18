package EhNew.data;

import EhNew.math.Vec3;

/**
 * @author Abhishek
 */
public class PointLight extends DirectionalLight{
    public Vec3 pos;
    public float fallOff, cutOff;
    private int idx;
    
    public PointLight(){
        super();
        pos = new Vec3();
        dir = null;
        fallOff = 0f;
        cutOff = 10f;
        idx = -1;
    }

    public int getIdx() {
        return idx;
    }
    public void setIdx(int idx) {
        this.idx = idx;
    }
}