package aggroforce.render;

import java.io.File;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;

import aggroforce.texture.Texture;

public class RenderBlocks {

	private FloatBuffer data = BufferUtils.createFloatBuffer(80000000);

	private int bid;
	private int verts = 0;
	private boolean uploaded=false;
	private float[] color = {0f,0f,0f,1f};
	private float[] normal = {0f,0f,0f};

	public RenderBlocks(){
		bid = ARBVertexBufferObject.glGenBuffersARB();
	}

	public void addVertexUV(float x, float y, float z, float u, float v){
		verts++;
		if(data.remaining()<8){
		}else{
			data.put(new float[] {x,y,z,u,v,normal[0],normal[1],normal[2]});
		}
	}

	public void setColor(float r, float g, float b, float a){
		this.color = new float[] {r,g,b,a};
	}

	public void setNormal(float x, float y, float z){
		this.normal = new float[] {x,y,z};
	}

	public void upload(){
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, bid);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, (FloatBuffer)data.flip(), ARBVertexBufferObject.GL_STREAM_DRAW_ARB);
	    GL11.glVertexPointer(3, GL11.GL_FLOAT, 8<<2, 0L);
	    GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 8<<2, 3<<2);
	    GL11.glNormalPointer(GL11.GL_FLOAT, 8<<2, 5<<2);
	    data = null;
	}

	public static Texture test = Texture.loadTextureFromFile("Stone", new File("resource/textures/blocks/terrain.png"));
	public void render(){
		if(!uploaded){
			this.upload();
			this.uploaded = true;
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, test.getGLTexID());
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, bid);
		GL11.glDrawArrays(GL11.GL_QUADS, 0, verts);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
	}
}
