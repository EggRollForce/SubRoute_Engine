package aggroforce.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

public class VBOTest {
	
	public static int genVBOID() {
		  if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
		    IntBuffer buffer = BufferUtils.createIntBuffer(1);
		    ARBVertexBufferObject.glGenBuffersARB(buffer);
		    return buffer.get(0);
		  }
		  return 0;
		}
	
	public static void addData(int id, float[] data){
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			FloatBuffer datab = BufferUtils.createFloatBuffer(data.length);
			datab.put(data);
			datab.flip();
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
			ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, datab, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
		}
	}
	public static void addElementData(int id, int[] data) {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			  IntBuffer datab = BufferUtils.createIntBuffer(data.length);
			  datab.put(data);
			  datab.flip();
			  ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, id);
			  ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, datab, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
		}
	}
	
	public static void renderVBO(int id){		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
		GL11.glVertexPointer(3,GL11.GL_FLOAT,0,0);
//		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, id);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
	}
	static void drawVertexBufferObject(){
	      // create geometry buffers
	      FloatBuffer cBuffer = BufferUtils.createFloatBuffer(9);
	      cBuffer.put(1).put(0).put(0);
	      cBuffer.put(0).put(1).put(0);
	      cBuffer.put(0).put(0).put(1);
	      cBuffer.flip();

	      FloatBuffer vBuffer = BufferUtils.createFloatBuffer(9);
	      vBuffer.put(-0.5f).put(-0.5f).put(0.0f);
	      vBuffer.put(+0.5f).put(-0.5f).put(0.0f);
	      vBuffer.put(+0.5f).put(+0.5f).put(0.0f);
	      vBuffer.flip();

	      //

	      IntBuffer ib = BufferUtils.createIntBuffer(2);

	      ARBVertexBufferObject.glGenBuffersARB(ib);
	      int vHandle = ib.get(0);
//	      int cHandle = ib.get(1);

	      GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
//	      GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

	      ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vHandle);
	      ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vBuffer, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
	      GL11.glVertexPointer(3, GL11.GL_FLOAT, /* stride */3 << 2, 0L);

//	      ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, cHandle);
//	      ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, cBuffer, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
//	      GL11.glColorPointer(3, GL11.GL_FLOAT, /* stride */3 << 2, 0L);

	      GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);

	      ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);

//	      GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
	      GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

	      // cleanup VBO handles
	      ib.put(0, vHandle);
//	      ib.put(1, cHandle);
	      ARBVertexBufferObject.glDeleteBuffersARB(ib);
	   }
	   static void preRender(){
	      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);

	      GL11.glMatrixMode(GL11.GL_MODELVIEW);
	      GL11.glLoadIdentity();
	   }
}
