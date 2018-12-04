package EhNew.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

/**
 * @since Apr 21, 2018
 * @author Abhishek
 */
public class Cut {
    
    public static void main(String[] args) throws IOException {
        findMissing();
    }
    
    static void findMissing(){
        File outdir = new File("C:\\Users\\abhia\\Desktop\\out");
        char n[] = "qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
        
        for(File f: outdir.listFiles()){
            char c = f.getName().charAt(0);
            for (int i = 0; i < n.length; i++) {
                if(c == n[i]){
                    n[i] = ' ';
                }
            }
        }
        System.out.println("Missing Characters: " + Arrays.toString(n));
    }
    
    public static void cut() throws IOException{
        File indir = new File("C:\\Users\\abhia\\Desktop\\input");
        System.out.println("Openened Directory Input");
        File outdir = new File("C:\\Users\\abhia\\Desktop\\out");
        System.out.println("Openened Directory Output");
        
        File list[] = indir.listFiles();
        
        System.out.println(list.length + " input files found.");
        int a = outdir.listFiles().length;
        
        for(File f: list){
            if(!f.isFile()) continue;
            char[] name = f.getName().toCharArray();
            BufferedImage image = ImageIO.read(f);
            BufferedImage out[] = new BufferedImage[5];
            
            int height = image.getHeight();
            int width = image.getWidth()/5;
            
            for(int j = 0; j < 5; j++){
                out[j] = image.getSubimage(j * width, 0, width, height);
                File d = new File("C:\\Users\\abhia\\Desktop\\out\\" + name[j] + "_" + (a++) + ".jpeg");
                d.createNewFile();
                ImageIO.write(out[j], "jpeg", d);
            }
            
        }
        
        System.out.println("Written " + a + " files. Done.");
    }

}
