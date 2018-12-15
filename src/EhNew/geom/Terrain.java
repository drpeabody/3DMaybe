package EhNew.geom;

import EhNew.Engine;
import EhNew.TexturedEntity;
import EhNew.math.Vec2;
import EhNew.math.Vec3;
import EhNew.shaders.Shader;
import EhNew.util.Texture;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

/**
 * @since Jul 8, 2017
 * @author Abhishek
 */
public class Terrain extends TexturedEntity {
    //Uses 16 bit Monochrome HeightMaps.
    private BufferedImage heightMap;
    private Vec3 startPos, cellSize, endPos;
    private Vec2 TextureScale;
    private final float del;

    public Terrain(BufferedImage highetmap, Vec3 cellSize, Vec3 startPos, Vec2 TexScale, Shader s) {
        super(s);
        this.heightMap = highetmap;
        if(heightMap == null){
            try {
                heightMap = ImageIO.read(Engine.class.getResourceAsStream("TestHeightMap.png"));
            } catch (IOException ignored) {}
        }
        this.cellSize = cellSize;
        this.startPos = startPos;
        TextureScale = TexScale;
        endPos = null;
        del = heightMap.getRGB(0,0);
    }

    public Terrain(BufferedImage highetmap, Vec3 cellSize, Vec3 startPos,
                   BufferedImage texture, BufferedImage normal, BufferedImage emmisive,
                   Vec2 texScale, Shader s) {
        super(s,
                (texture == null)? null: new Texture(GL_TEXTURE_2D, s.getDiffuseMapTextureUnit(), texture),
                (normal == null)? null: new Texture(GL_TEXTURE_2D, s.getNormalMapTextureUnit(), normal),
                (emmisive == null)? null: new Texture(GL_TEXTURE_2D, s.getEmmisiveMapTextureUnit(), emmisive));
        this.heightMap = highetmap;
        this.cellSize = cellSize;
        TextureScale = texScale;
        this.startPos = startPos;
        endPos = null;
        if(heightMap == null){
            try {
                heightMap = ImageIO.read(Engine.class.getResourceAsStream("TestHeightMap.png"));
            } catch (IOException ignored) {}
        }
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
        super.load();
        drawMode = GL11.GL_QUADS;
        
        final int width = heightMap.getWidth();
        final int height = heightMap.getHeight();
        final int[] arr = new int[(width - 1) * (height - 1) * 4];
        
        Vertex v[] = new Vertex[height*width];
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Vertex t = v[i*width + j] = new Vertex();
                t.pos = new Vec3(startPos.x + cellSize.x * i,
                        startPos.y + cellSize.y * (heightMap.getRGB(i, j) - del),
                        startPos.z + cellSize.z * j);
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
                    v[udx].normal = v[udx].tangent.cross(v[udx + 1].pos.difference(v[udx - 1].pos));
                }

                v[udx].normal.normalize();
                v[udx].tangent.normalize();
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
        
        load(Vertex.getDataFrom(v), arr);
    }
}
