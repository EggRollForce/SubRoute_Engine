package aggroforce.texture;

import java.awt.image.BufferedImage;

import java.io.File;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class Texture {

	private final int texId;
	private String name;
	private int w,h;

	private Texture(String name, BufferedImage img){
		//Get image info
		w = img.getWidth();
		h = img.getHeight();
		//Get the pixel color data
		int[] rgba = new int[img.getWidth()*img.getHeight()];
		img.getRGB(0, 0, img.getWidth(), img.getHeight(), rgba, 0, img.getWidth());
		//Create the data buffer and generate the GL texture object
		ByteBuffer idat = BufferUtils.createByteBuffer(rgba.length*4);
		this.texId=GL11.glGenTextures();
		//Transfer pixel color data to the buffer
		for(int i = 0; i<rgba.length; i++){
			int c = rgba[i];
			idat.put((byte)((c>>16)&0xff));
			idat.put((byte)((c>>8)&0xff));
			idat.put((byte)(c&255));
			idat.put((byte)((c>>24)&0xff));
		}
		idat.flip();
		//Finalize the texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texId);

		//Clamp texture
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		//Turn filtering off
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		//Upload data to OpenGL
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, img.getWidth(), img.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, idat);
	}

	public int getGLTexID(){
		return texId;
	}

	public String getName(){
		return name;
	}

	public int getWidth(){
		return w;
	}
	public int getHeight(){
		return h;
	}

	public static Texture loadTextureFromFile(String name, File file){
		BufferedImage img;
		try{
			img = ImageIO.read(file);
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("Error occured during texture load!");
			return null;
		}
		return new Texture(name,img);
	}

	public static Texture loadBufferedImage(String name,BufferedImage img){
		return new Texture(name,img);
	}

	public void deleteTexture(){
		GL11.glDeleteTextures(texId);
	}

}
