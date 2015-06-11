package subroute.render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Renderer {

	private boolean updated = false;
	private int verts = 0;
	private int oldVerts = 0;
	private int bufofst = -1;
	private int bufid = 0;
	private float[] normal = {0f,0f,0f};
	private static float[] data = new float[6291456];
	private static ByteBuffer bb = BufferUtils.createByteBuffer(0);
	private static FloatBuffer fb = bb.asFloatBuffer();
	private IRenderable r;
	private IUpdateable vbo;
	private float[] temp =  new float[8];

	public Renderer(IRenderable renderable){
		r = renderable;
	}
	public void addVertexUV(float x, float y, float z, float u, float v){
		temp[0] = x;
		temp[1] = y;
		temp[2] = z;
		temp[3] = u;
		temp[4] = v;
		temp[5] = normal[0];
		temp[6] = normal[1];
		temp[7] = normal[2];
		for(int i = 0; i<8; i++){
			data[verts*8+i] = temp[i];
		}
		verts++;
	}

	public void setNormal(float x, float y, float z){
		this.normal[0]=x;
		this.normal[1]=y;
		this.normal[2]=z;
	}

	public ByteBuffer getData(){
		fb.clear();
		if(data!=null&&verts*8>fb.capacity()){
			oldVerts=verts;
			bb = BufferUtils.createByteBuffer(verts*Float.SIZE);
			fb = bb.asFloatBuffer();
			fb.put(data,0,verts*8);
			fb.flip();
		}else if(data!=null){
			fb = bb.asFloatBuffer();
			fb.put(data, 0, verts*8);
			fb.flip();
		}
		System.out.println(verts*8*Float.SIZE/8/1000);
		return bb;
	}

	public float[] getArrayData(){
		if(data!=null){
			return data;
		}
		return null;
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
	public boolean isMarkedForUpdate(){
		return this.updated;
	}
	public void markForUpdate(boolean updated){
		if(this.vbo!=null&&updated){
			vbo.markForUpdate();
		}
		this.updated = updated;
	}
	protected void setBufferId(int id){
		this.bufid = id;
	}
	protected int getBufferOffset(){
		return bufofst;
	}
	protected void setBufferOffset(int offset){
		this.bufofst = offset;
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
