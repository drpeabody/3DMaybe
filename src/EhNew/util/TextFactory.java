package EhNew.util;

import EhNew.math.Vec4;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author Abhishek
 * @since 5-1-2016
 * @version 0.16.1.07
 */
public class TextFactory{
    public Vec4 color;
    public Font font;
    public FontRenderContext context;
    
    public TextFactory(){
        color = new Vec4(1f, 1f, 1f, 1f);
        font = new Font("Dialog.plain", 0, 16);
        context = new FontRenderContext(null, true, true);
    }
    
    public void setSize(int fontSize){
        font = new Font(font.getName(), font.getStyle(), fontSize);
    }
    public void setColor(Vec4 color){
        this.color = color;
    }
    public void setState(Vec4 c, int fontStyle, int fontSize){
        if(c == null){
            c = new Vec4(1f, 1f, 1f, 1f);
        }
        color = c;
        font = new Font(font.getName(), fontStyle, fontSize);
    }
    public void setState(Vec4 c, Font f){
        if(c == null){
            c = new Vec4(1f, 1f, 1f, 1f);
        }
        if(f == null){
            f = new Font("Dialog.plain", 0, 14);
        }
        color = c;
        font = f;
    }
    
    public BufferedImage createText(String c){
        TextLayout t = new TextLayout(c, font, context);
        Rectangle2D r = t.getBounds();
        r.add((-t.getAscent()/2), t.getAscent());
        
        BufferedImage b = new BufferedImage((int)r.getWidth(), (int)r.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = b.createGraphics();
        g.setFont(font);
        g.setColor(new java.awt.Color(color.r, color.g, color.b, color.a));
        g.drawString(c, 0, b.getHeight() - (t.getAscent()/2));
        
        return b;
    }
}