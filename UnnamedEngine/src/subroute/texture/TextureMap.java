package subroute.texture;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TextureMap {

	public static final TextureMap blockMap = new TextureMap(1024,1024);

	private ArrayList<TextureTile> tileMap = new ArrayList<TextureTile>();

	private int texid = -1,width,height,nxtx,nxty;
	private boolean uploaded = false;

	public TextureMap(int width, int height){
		this.width = width;
		this.height = height;
		if(texid == -1){
			texid = GL11.glGenTextures();
		}
	}

	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}

	public void bindTextureMap(){
		if(!uploaded){
			this.uploadImgData();
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texid);
	}

	public TextureTile addAlignTile(TextureTile textile){
		if(!this.tileMap.contains(textile)){
			if(nxtx+textile.getWidth()>width){
				nxtx = 0;
				nxty += textile.getHeight();
				textile.setXY(nxtx, nxty);
			}else{
				textile.setXY(nxtx, nxty);
				nxtx += textile.getWidth();
			}
			this.tileMap.add(textile);
			return textile;
		}
		return null;
	}

	public void uploadImgData(){
		uploaded = true;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texid);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		ByteBuffer zbuf = BufferUtils.createByteBuffer(width*height*4);
		BufferUtils.zeroBuffer(zbuf);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, zbuf);
		for(TextureTile tex : tileMap){
			tex.uploadTexture();
		}
	}

}
