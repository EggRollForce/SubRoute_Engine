package aggroforce.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;

public class RenderBlocks {

	FloatBuffer data = BufferUtils.createFloatBuffer(10000000);

	private int bid;
	private int verts = 0;
	private boolean uploaded=false;

	public RenderBlocks(){
		bid = ARBVertexBufferObject.glGenBuffersARB();
	}

	public void addVTN(float x, float y, float z, float u, float v, float nx, float ny, float nz){
		if(data.remaining()<8){
			data.limit(data.limit()+8);
			data.put(new float[] {x,y,z/*,u,v,nx,ny,nz*/});
		}else{
			data.put(new float[] {x,y,z/*,u,v,nx,ny,nz*/});
		}
	}

	public void upload(){
		verts = (data.limit()-data.remaining());
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, bid);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, (FloatBuffer)data.flip(), ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
	    GL11.glVertexPointer(3, GL11.GL_FLOAT, 3<<2, 0L);
//	      GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 8, 0L);
//	      GL11.glNormalPointer(GL11.GL_FLOAT, 8, 0L);
	}

	public void render(){
		if(!uploaded){
			this.upload();
			this.uploaded = true;
		}
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, bid);
		GL11.glDrawArrays(GL11.GL_QUADS, 0, verts);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
	}
}
