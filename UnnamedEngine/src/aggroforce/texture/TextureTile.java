package aggroforce.texture;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class TextureTile implements Icon{

	private int x=0,y=0,width,height;

	private ByteBuffer img;

	private TextureTile(int width, int height, ByteBuffer img){
		this.width = width;
		this.height = height;
		this.img = img;
	}

	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}

	@Override
	public float getStartU() {
		return ((x)/256f)+guard;
	}

	@Override
	public float getStartV() {
		return ((y)/256f)+guard;
	}

	@Override
	public float getEndU() {
		return ((x+width)/256f)-guard;
	}

	@Override
	public float getEndV() {
		return ((y+height)/256f)-guard;
	}

	public static TextureTile loadImgData(TextureMap map, int x, int y, BufferedImage img){
		int width = img.getWidth();
		int height = img.getHeight();
		int[] rgba = new int[width*height];
		img.getRGB(0, 0, width, height, rgba, 0, width);
		ByteBuffer idat = BufferUtils.createByteBuffer(rgba.length*4);
		for(int i = 0; i<rgba.length; i++){
			int c = rgba[i];
			idat.put((byte)((c>>16)&0xff));
			idat.put((byte)((c>>8)&0xff));
			idat.put((byte)(c&255));
			idat.put((byte)((c>>24)&0xff));
		}
		idat.flip();
		return map.addAlignTile(new TextureTile(width,height,idat));
	}

	public ByteBuffer getData(){
		return img;
	}

	public void uploadTexture(){
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x, y, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, img);
	}
}
