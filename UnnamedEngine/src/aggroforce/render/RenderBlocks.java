package aggroforce.render;

import java.io.File;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;

import aggroforce.render.camera.BlockTarget;
import aggroforce.render.camera.Camera;
import aggroforce.texture.Texture;
import aggroforce.world.storage.WorldStorage;

public class RenderBlocks {

	private static FloatBuffer data;

	private int bid;
	private int verts = 0;
	private boolean uploaded=false;
	private float[] color = {0f,0f,0f,1f};
	private float[] normal = {0f,0f,0f};

	public RenderBlocks(){
		bid = ARBVertexBufferObject.glGenBuffersARB();
		if(data==null){
			data = BufferUtils.createFloatBuffer(80000000);
		}
	}

	public void upload(){
		for(Renderer render: WorldStorage.getRenderers()){
			data.put(render.getData());
			verts+=render.getVerts();
		}
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

	public void renderBlockOutline(){
		BlockTarget t = Camera.instance.getLookTargetBlock();
		System.out.println("Returning "+t);
		if(t!=null){
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glColor3f(1f, 1f, 1f);
			GL11.glVertex3f(t.x, t.y, t.z);
			GL11.glVertex3f(t.x, t.y+1, t.z);
			GL11.glVertex3f(t.x, t.y+1, t.z+1);
			GL11.glVertex3f(t.x, t.y, t.z+1);
			GL11.glEnd();
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex3f(t.x+1, t.y, t.z+1);
			GL11.glVertex3f(t.x+1, t.y+1, t.z+1);
			GL11.glVertex3f(t.x+1, t.y+1, t.z);
			GL11.glVertex3f(t.x+1, t.y, t.z);
			GL11.glEnd();
		}
	}
}
