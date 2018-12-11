package EhNew.util;

import EhNew.DrawableEntity;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Abhishek
 * @since 09 Dec, 2018
 */

public class DrawList {
    //There is a one to one index correspondence
    CopyOnWriteArrayList<DrawableEntity> meshes;
    DrawableEntity mesh;

    public DrawList(DrawableEntity mesh){
        this.mesh = mesh;
        meshes = new CopyOnWriteArrayList<>();
    }

    //Conveniently Returns the Added Entity
    public DrawableEntity add(DrawableEntity d){
        meshes.add(d);
        d.loadDataFrom(mesh);
        return d;
    }

    public void remove(DrawableEntity d){
        meshes.remove(d);
    }

    public void load(){
        mesh.load();
    }

    public void draw(){
        for(DrawableEntity d: meshes){
            d.draw();
        }
    }

    public void update(){
        mesh.update();
        for(DrawableEntity d: meshes){
            d.update();
        }
    }

    public void destroy(){
        mesh.destroy();
    }

    public void forEach(DrawableConsumer c) {
        for(DrawableEntity d: meshes){
            c.consume(d);
        }
    }

    public interface DrawableConsumer{
        void consume(DrawableEntity d);
    }
}
