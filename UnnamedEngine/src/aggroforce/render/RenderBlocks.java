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

	float off = 0.001f;
	public void renderBlockOutline(){
		BlockTarget t = Camera.instance.getLookTargetBlock();
		if(t!=null){
			GL11.glColor4f(0f, 0f, 0f, 0.5f);
			GL11.glLineWidth(2f);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex3f(t.x-off, t.y-off, t.z-off);
			GL11.glVertex3f(t.x-off, t.y+1+off, t.z-off);
			GL11.glVertex3f(t.x-off, t.y+1+off, t.z+1+off);
			GL11.glVertex3f(t.x-off, t.y-off, t.z+1+off);
			GL11.glEnd();
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex3f(t.x+1+off, t.y-off, t.z+1+off);
			GL11.glVertex3f(t.x+1+off, t.y+1+off, t.z+1+off);
			GL11.glVertex3f(t.x+1+off, t.y+1+off, t.z-off);
			GL11.glVertex3f(t.x+1+off, t.y-off, t.z-off);
			GL11.glEnd();
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3f(t.x-off, t.y-off, t.z-off);
			GL11.glVertex3f(t.x+1+off, t.y-off, t.z-off);
			GL11.glVertex3f(t.x-off, t.y+1+off, t.z-off);
			GL11.glVertex3f(t.x+1+off, t.y+1+off, t.z-off);
			GL11.glVertex3f(t.x-off, t.y+1+off, t.z+1+off);
			GL11.glVertex3f(t.x+1+off, t.y+1+off, t.z+1+off);
			GL11.glVertex3f(t.x-off, t.y-off, t.z+1+off);
			GL11.glVertex3f(t.x+1+off, t.y-off, t.z+1+off);
			GL11.glEnd();
		}
	}
}
