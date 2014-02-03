package aggroforce.render;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class Renderer {
	private FloatBuffer data;

	private boolean isDone = false;
	private boolean updated = false;
	private int verts=0;
	private float[] normal = {0f,0f,0f};

	public Renderer(){
		if(data == null){
			data = BufferUtils.createFloatBuffer(800000);
		}
	}
	public void addVertexUV(float x, float y, float z, float u, float v){
		data.put(new float[] {x,y,z,u,v,normal[0],normal[1],normal[2]});
		verts++;
	}

	public void setNormal(float x, float y, float z){
		this.normal = new float[] {x,y,z};
	}

	public FloatBuffer getData(){
		if(!this.isDone){
			compile();
		}
		return this.data.asReadOnlyBuffer();
	}

	public int getVerts(){
		return verts;
	}
	public void compile(){
		this.isDone = true;
		data.flip();
	}
	public void reset(){
		verts = 0;
		this.isDone = false;
		data.flip();
		data.clear();
	}
	public boolean isUpdated(){
		return this.updated;
	}
	public void setUpdated(boolean updated){
		this.updated = updated;
	}
}
