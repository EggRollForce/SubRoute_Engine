package aggroforce.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class VBO {
	public boolean vboActive = false;
	public static final int TYPE_VERTEX = 0;
	public static final int TYPE_COLOR = 1;
	public static final int TYPE_INDEXED = 2;
	public static final int DATA_VERT = 50;
	public static final int DATA_COLOR = 51;
	public static final int DATA_INDEX = 52;
	private int type;
	public int id;
	public VBO(int type){
		this.type = type;
	}
	public void genVBO(){
		if(GLContext.getCapabilities().GL_ARB_vertex_buffer_object){
			int bufsz = 0;
			switch(type){
				case TYPE_VERTEX:
					bufsz++;
					break;
				case TYPE_COLOR:
					break;
				case TYPE_INDEXED:
					break;
			}
			if(bufsz>0){
				IntBuffer buf = BufferUtils.createIntBuffer(bufsz);
				ARBVertexBufferObject.glGenBuffersARB(buf);
				id=buf.get(0);
			}
		}
	}
	public void addData(int data, float[] indata){
		if(GLContext.getCapabilities().GL_ARB_vertex_buffer_object){
			FloatBuffer dat = BufferUtils.createFloatBuffer(indata.length);
			for(int x = 0; x < indata.length; x++){
				dat.put(indata[x]);
			}
			dat.flip();
			switch(data){
				case DATA_VERT:
					ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
					ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, dat, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
					GL11.glVertexPointer(3, GL11.GL_FLOAT, 3 << 2, 0L);
					break;
				case DATA_COLOR:
					
					break;
				case DATA_INDEX:
					break;
			}
		}
	}
	public void render(){
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
		GL11.glDisable(GL11.GL_VERTEX_ARRAY);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
}
