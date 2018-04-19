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
    protected Texture tex, texNormal, emm;
    protected Vec3 startPos, cellSize, endPos;
    protected Vec2 TextureScale;
    private final float del;

    public Terrain(BufferedImage highetmap, Vec3 cellSize, Vec3 startPos, Vec2 TexScale, Shader s) {
        super(s);
        this.heightMap = highetmap;
        if(heightMap == null){
            try {
                heightMap = ImageIO.read(Engine.class.getResourceAsStream("TestHeightMap.png"));
            } catch (IOException ex) {}
        }
        this.cellSize = cellSize;
        this.startPos = startPos;
        tex = new Texture(GL_TEXTURE_2D, s.getDiffuseMapTextureUnit(), "null.png");
        texNormal = new Texture(GL_TEXTURE_2D, s.getNormalMapTextureUnit(),"nullN.png");
        emm = new Texture(GL_TEXTURE_2D, s.getEmmisiveMapTextureUnit(),"nullE.png");
        TextureScale = TexScale;
        endPos = null;
        del = heightMap.getRGB(0,0);
    }
    public Terrain(BufferedImage highetmap, Vec3 cellSize, Vec3 startPos, BufferedImage texture, BufferedImage normal, Vec2 texScale, Shader s) {
        super(s);
        this.heightMap = highetmap;
        this.cellSize = cellSize;
        TextureScale = texScale;
        this.startPos = startPos;
        tex = new Texture(GL_TEXTURE_2D, s.getDiffuseMapTextureUnit(), texture);
        texNormal = new Texture(GL_TEXTURE_2D, s.getNormalMapTextureUnit(), normal);
        emm = new Texture(GL_TEXTURE_2D, s.getEmmisiveMapTextureUnit(), "nullE.png");
        endPos = null;
        del = heightMap.getRGB(0,0);
    }
    public Terrain(BufferedImage highetmap, Vec3 cellSize, Vec3 startPos, BufferedImage texture, BufferedImage normal, BufferedImage emmisive, Vec2 texScale, Shader s) {
        super(s);
        this.heightMap = highetmap;
        this.cellSize = cellSize;
        TextureScale = texScale;
        this.startPos = startPos;
        tex = new Texture(GL_TEXTURE_2D, s.getDiffuseMapTextureUnit(), texture);
        texNormal = new Texture(GL_TEXTURE_2D, s.getNormalMapTextureUnit(), normal);
        emm = new Texture(GL_TEXTURE_2D, s.getEmmisiveMapTextureUnit(), emmisive);
        endPos = null;
        del = heightMap.getRGB(0,0);
    }

    public Vec3 getStartPos() {
        return startPos;
    }

    public Vec3 getCellSize() {
        return cellSize;
    }

    public Vec3 getEndPos() {
        return endPos;
    }

    public Vec2 getTextureScale() {
        return TextureScale;
    }
    
    public Vec3 getCenter(){
        return startPos.sum(endPos).product(0.5f);
    }
    
    public float getHeightAt(float x, float y){
        final int idx = (int)Math.floor(x), idy = (int)Math.floor(y);
        final float ref = heightMap.getRGB(idx, idy);
        final float dzx = ((float)heightMap.getRGB(idx + 1, idy) - ref) * (x - idx),
            dzy = ((float)heightMap.getRGB(idx, idy + 1) - ref) * (y - idy);
        return ref + dzx + dzy - del;
    }
    
    @Override
    public void load() {
        tex.loadFromImage();
        tex.bufferData();
        texNormal.loadFromImage();
        texNormal.bufferData();
        emm.loadFromImage();
        emm.bufferData();
        
        final int width = heightMap.getWidth();
        final int height = heightMap.getHeight();
        final int[] arr = new int[(width - 1) * (height - 1) * 4];
        
        Vertex v[] = new Vertex[height*width];
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Vertex t = v[i*width + j] = new Vertex();
                t.pos = new Vec3(startPos.x + cellSize.x * i, startPos.y + cellSize.y * (heightMap.getRGB(i, j) - del), startPos.z + cellSize.z * j);
                t.TextCoods.x = TextureScale.x * i;
                t.TextCoods.y = TextureScale.y * j;
            }
        }
        
        endPos = v[(width - 1) * width + height - 1].pos.product(1f);
        
        int udx = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++, udx++) {
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
        
        udx = 0;
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
    public void draw(int drawMode) {
        texNormal.bind();
        tex.bind();
        emm.bind();
        super.draw(GL11.GL_QUADS);
        tex.unBind();
        emm.unBind();
        texNormal.unBind();
    }
    
    @Override
    public void destroy(){
        super.destroy();
        tex.destroy();
        texNormal.destroy();
        emm.destroy();
    }
}
