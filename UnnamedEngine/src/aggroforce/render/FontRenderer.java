package aggroforce.render;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.lwjgl.opengl.GL11;

import aggroforce.texture.Texture;

public class FontRenderer {

	private static Texture fontTexture;
	private static File ttf = new File("resource/font/font.ttf");
	private static File chars = new File("resource/font/font.txt");
	private static Font font = null;
	private static FontMetrics fmet = null;
	private static String charmap = "";
	private final int adv = 9;
	private final int asc = 10;

	public FontRenderer(){
		if(FontRenderer.fontTexture==null){
			FontRenderer.fontTexture = Texture.loadBufferedImage("font", this.genFontMap());
		}
	}

	public void reloadFontTexture(){
		fontTexture.deleteTexture();
		FontRenderer.fontTexture = Texture.loadBufferedImage("font", this.genFontMap());
	}

	//Debug purposes only!
	public void useFontTexture(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTexture.getGLTexID());
	}



	private BufferedImage genFontMap(){
		BufferedImage img = new BufferedImage(128,128,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		font = g2d.getFont();

		BufferedReader br = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, ttf).deriveFont(8f);
			g2d.setFont(font);
			br = new BufferedReader(new FileReader(chars));
			String ln;
			while((ln=br.readLine())!=null){
				if(!ln.startsWith("#")){
					charmap+=ln.toString();
				}
			}
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		fmet = g2d.getFontMetrics();
		int row = 0;
		String ln = "";
		for(int i = 0; i < charmap.length(); i++){
			if(asc*row+asc*2>=img.getHeight()){
				break;
			}
			if(ln.length()*adv+adv>=img.getWidth()){
				ln = "";
				row++;
			}
			g2d.drawString(""+charmap.charAt(i), ln.length()*adv, asc*row+asc);
			ln += charmap.charAt(i);
		}
		return img;
	}

	public int renderChar(char c,int x, int y, float size){
		int id = charmap.indexOf(c);
		int x2 = (int) ((id-Math.floor(id/14d)*14)*adv);
		int y2 = (int) ((Math.floor(id/14d)*asc));

		int w = fmet.stringWidth(c+"")-1;

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		useFontTexture();
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glTexCoord2f((x2+w)/128f, (y2+1)/128f);
		GL11.glVertex2d(x+w*size,y+asc*size);
		GL11.glTexCoord2f(x2/128f, (y2+1)/128f);
		GL11.glVertex2d(x,y+asc*size);
		GL11.glTexCoord2f((x2)/128f, (y2+1+asc)/128f);
		GL11.glVertex2d(x, y);
		GL11.glTexCoord2f((x2+w)/128f, (y2+1+asc)/128f);
		GL11.glVertex2d(x+w*size, y);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
		return w+fmet.getLeading();

	}

	public void drawString(String s, int x, int y, float size){
		float xoffset = 0;
		for(int i = 0; i < s.length(); i++){
			xoffset=xoffset+this.renderChar(s.charAt(i), (int) (x+xoffset), y, size)*size;
		}
	}
}
