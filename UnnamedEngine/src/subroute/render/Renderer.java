package subroute.render;

import java.nio.FloatBuffer;
import java.util.Arrays;

import org.lwjgl.BufferUtils;

public class Renderer {

	private boolean updated = false;
	private int verts = 0;
	private int oldVerts = 0;
	private int bufid = 0;
	private float[] normal = {0f,0f,0f};
	private float[] data;
	private FloatBuffer fb;
	private IRenderable r;
	private IUpdateable vbo;

	public Renderer(IRenderable renderable){
		r = renderable;
		if(data == null){
			data = new float[1310720];
			fb = BufferUtils.createFloatBuffer(0);
		}
	}
	public void addVertexUV(float x, float y, float z, float u, float v){
		if(data == null){
			data = new float[1310720];
		}
		if(verts*8+8>data.length){
			float[] olddat = Arrays.copyOf(data, data.length+800);
			data = null;
			data = olddat;
			olddat = null;

		}
		float[] temp = new float[] {x,y,z,u,v,normal[0],normal[1],normal[2]};
		for(int i = 0; i<8; i++){
			data[verts*8+i] = temp[i];
		}
		verts++;
	}

	public void setNormal(float x, float y, float z){
		this.normal = new float[] {x,y,z};
	}

	public FloatBuffer getData(){
		if(data!=null&&verts>oldVerts){
			oldVerts=verts;
			fb = (FloatBuffer)BufferUtils.createFloatBuffer(verts*8).put(data,0,verts*8).flip();
		}else if(data!=null){
			fb.put(data, 0, verts*8).flip();
		}
		data = null;
		return fb;
	}

	public int getVerts(){
		return verts;
	}
	public void reset(){
		verts = 0;
		if(fb!=null){
			fb.clear();
		}
	}
	public boolean isUpdated(){
		return this.updated;
	}
	public void setUpdated(boolean updated){
		if(this.vbo!=null&&updated){
			vbo.markForUpdate();
		}
		this.updated = updated;
	}
	protected void setBufferId(int id){
		this.bufid = id;
	}
	protected int getBufferId(){
		return this.bufid;
	}
	public void updateRenderable(){
		this.r.renderTo(this);
	}
	public Renderer setUpdateable(IUpdateable vbo){
		this.vbo = vbo;
		return this;
	}
}
