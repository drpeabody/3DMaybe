package EhNew.geom;

import EhNew.DrawableEntity;
import EhNew.Engine;
import EhNew.math.Vec2;
import EhNew.math.Vec3;
import EhNew.shaders.Shader;
import EhNew.util.Texture;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import org.lwjgl.opengl.GL13;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

/**
 * @since Jul 8, 2017
 * @author Abhishek
 */
public class Terrain extends DrawableEntity{
    //Uses 16 bit Monochrome HeightMaps.
    protected BufferedImage heightMap;
    protected Texture tex, texNormal;
    protected Vec3 startPos, cellSize;
    protected Vec2 TextureScale;

    public Terrain(BufferedImage highetmap, Vec3 cellSize, Vec3 startPos, Vec2 TexScale, Shader s) {
        super(s);
        this.heightMap = highetmap;
        this.cellSize = cellSize;
        this.startPos = startPos;
        tex = new Texture(GL_TEXTURE_2D, s.getDiffuseMapTextureUnit(), "null.png");
        texNormal = new Texture(GL_TEXTURE_2D, s.getNormalMapTextureUnit(),"nullN.png");
        TextureScale = TexScale;
    }
    public Terrain(BufferedImage highetmap, Vec3 cellSize, Vec3 startPos, BufferedImage texture, BufferedImage normal, Vec2 texScale, Shader s) {
        super(s);
        this.heightMap = highetmap;
        this.cellSize = cellSize;
        TextureScale = texScale;
        this.startPos = startPos;
        tex = new Texture(GL_TEXTURE_2D, s.getDiffuseMapTextureUnit(), texture);
        texNormal = new Texture(GL_TEXTURE_2D, s.getNormalMapTextureUnit(), normal);
    }

    @Override
    public void load() {
        if(heightMap == null){
            try {
                heightMap = ImageIO.read(Engine.class.getResourceAsStream("TestHeightMap.png"));
            } catch (IOException ex) {}
        }
        tex.loadFromImage();
        tex.bufferData();
        texNormal.loadFromImage();
        texNormal.bufferData();
        
        int width = heightMap.getWidth();
        int height = heightMap.getHeight();
        int[] arr = new int[(width - 1) * (height - 1) * 4];
        
        Vertex v[] = new Vertex[height*width];
        
        float a;
        float del = heightMap.getRGB(0, 0) & 0x0000ffff;
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                a = ((heightMap.getRGB(i, j)) & 0x0000ffff) - del;
                Vertex t = v[i*width + j] = new Vertex();
                t.pos = new Vec3(startPos.x + cellSize.x * i, startPos.y + cellSize.y * a, startPos.z + cellSize.z * j);
                t.TextCoods.x = TextureScale.x * i;
                t.TextCoods.y = TextureScale.y * j;
            }
        }
        int udx;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                udx = i * width + j;
                if(i == 0){
                    v[udx].tangent = v[(udx + width)].pos.difference(v[udx].pos);
                    v[udx].normal = v[udx + width].pos.difference(v[udx].pos).cross(v[udx + 1].pos.difference(v[udx].pos));
                } else if(i == width - 1){
                    v[udx].tangent = v[udx - 1].tangent;
                    v[udx].normal = v[udx - width].pos.difference(v[udx].pos).cross(v[udx - 1].pos.difference(v[udx].pos));
                }else {
                    v[udx].tangent = v[(udx) + width].pos.difference(v[udx - width].pos);
                    v[udx].normal.x = v[udx + width].pos.y - v[udx - width].pos.y;
                    v[udx].normal.z = -v[udx + 1].pos.y + v[udx - 1].pos.y;
                    v[udx].normal.y = -2f;
                    v[udx].normal.normalize();
                }
            }
        }
        
        int b = 0;
        
        for (int i = 0; i < width-1; i++) {
            for (int j = 0; j < height-1; j++) {
                udx = i * width + j;
                arr[b++] = udx;
                arr[b++] = udx + 1;
                arr[b++] = udx + 1 + width;
                arr[b++] = udx + width;
            }
        }
        
        IntBuffer idx = Vertex.getDataFrom(arr);
        FloatBuffer vert = Vertex.getDataFrom(v);
        idx.flip();
        vert.flip();
        
        indexCount = idx.limit();
        indexOffset = 0;
        idxID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, idx, GL_STATIC_DRAW);

        vertID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertID);
        glBufferData(GL_ARRAY_BUFFER, vert, GL_STATIC_DRAW);
    }

    @Override
    public void draw() {
        texNormal.bind();
        tex.bind();
        super.draw(GL11.GL_QUADS);
        tex.unBind();
        texNormal.unBind();
    }
    
    @Override
    public void destroy(){
        super.destroy();
        tex.destroy();
        texNormal.destroy();
    }
}
