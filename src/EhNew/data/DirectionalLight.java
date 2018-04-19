package EhNew.data;

import EhNew.math.Vec3;

/**
 * @since 28 Jan, 2016
 * @author Abhishek
 */
public class DirectionalLight{
    public Vec3 color, dir;
    public float ambInten, diffInten;
    public float specInten, specPower;
    
    public DirectionalLight(){
        color = new Vec3(1.0f,1.0f,1.0f);
        dir = new Vec3(-1.0f, -0.5f, 0.0f);
        ambInten = 0.1f;
        diffInten = 0.5f;
        specInten = 2f;
        specPower = 8f;
    }
}