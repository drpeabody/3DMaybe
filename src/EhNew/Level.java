package EhNew;

/**
 * @author Abhishek
 */
public abstract class Level {
    
    protected Engine engine;
    
    public Level(Engine main){
        engine = main;
    }
    
    public void load(){}
    public abstract void draw();
    public abstract void update();
    public abstract void destroy();
}