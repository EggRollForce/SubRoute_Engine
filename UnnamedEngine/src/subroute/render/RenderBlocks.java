package subroute.render;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import subroute.input.KeyboardReader;
import subroute.render.camera.BlockTarget;
import subroute.render.camera.Camera;
import subroute.texture.TextureMap;
import subroute.world.storage.WorldStorage;


public class RenderBlocks {
	private static final int MAX_VBOS = 10;
	private static IntBuffer vbos = BufferUtils.createIntBuffer(MAX_VBOS);
	public static final double size = 0.25;
	public static final long bufferSize = (long) (((8590000000L*size)/Float.SIZE)/MAX_VBOS);
	private ArrayList<VertexBufferObject> VBOS = new ArrayList<VertexBufferObject>(MAX_VBOS);

	public RenderBlocks(){
		GL15.glGenBuffers(vbos);
		for(int i = 0; i < vbos.capacity(); i++){
			VBOS.add(new VertexBufferObject(vbos.get(i),bufferSize));
		}
	}

	public void addRenderer(Renderer r){
		for(VertexBufferObject vbo : VBOS){
			if(vbo.hasRoomForRenderer(r)){
				System.out.println("Adding renderer to vbo id:"+vbo.bufid);
				vbo.addRenderer(r);
				return;
			}
		}
		System.err.println("Can't add renderer!");
	}

	public void render(){
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		TextureMap.blockMap.bindTextureMap();
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		for(VertexBufferObject vbo : VBOS){
			if(vbo.isMarkedForUpdate()){
				vbo.reupload();
			}
			vbo.render();
		}
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
	}
	int lastx=0,lasty=0;
	public void checkForSegGen(){
		double cx;
		double cz;
		if(Camera.getBoundEntity() != null){
			cx = (Camera.getBoundEntity().getXPos()/16d);
			cz = (Camera.getBoundEntity().getZPos()/16d);
		}else{
			cx = (Camera.x/16d);
			cz = (Camera.z/16d);
		}
		if(!((int)cx==lastx&&(int)cz==lasty)){
			lastx = (int)cx;
			lasty = (int)cz;
			WorldStorage.getInstance().needCheck(lastx, lasty);
		}
		WorldStorage.getInstance().checkGenRadius();
	}

	float off = 0.001f;
	boolean b1state = false;
	boolean b2state = false;
	int id = 1;

	public void renderBlockOutline(){
		BlockTarget t = Camera.instance.getLookTargetBlock();
		if(t!=null){
			GL11.glColor4f(0f, 0f, 0f, 0.5f);
			GL11.glLineWidth(3f);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex3f(t.x-off, t.y+off, t.z-off);
			GL11.glVertex3f(t.x-off, t.y-1-off, t.z-off);
			GL11.glVertex3f(t.x-off, t.y-1-off, t.z+1+off);
			GL11.glVertex3f(t.x-off, t.y+off, t.z+1+off);
			GL11.glEnd();
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex3f(t.x+1+off, t.y+off, t.z+1+off);
			GL11.glVertex3f(t.x+1+off, t.y-1-off, t.z+1+off);
			GL11.glVertex3f(t.x+1+off, t.y-1-off, t.z-off);
			GL11.glVertex3f(t.x+1+off, t.y+off, t.z-off);
			GL11.glEnd();
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3f(t.x-off, t.y+off, t.z-off);
			GL11.glVertex3f(t.x+1+off, t.y+off, t.z-off);
			GL11.glVertex3f(t.x-off, t.y-1-off, t.z-off);
			GL11.glVertex3f(t.x+1+off, t.y-1-off, t.z-off);
			GL11.glVertex3f(t.x-off, t.y-1-off, t.z+1+off);
			GL11.glVertex3f(t.x+1+off, t.y-1-off, t.z+1+off);
			GL11.glVertex3f(t.x-off, t.y+off, t.z+1+off);
			GL11.glVertex3f(t.x+1+off, t.y+off, t.z+1+off);
			GL11.glEnd();
			if(Character.isDigit(KeyboardReader.lastChar)){
				id = Integer.parseInt(""+KeyboardReader.lastChar);
			}
			if(!b1state&&Mouse.isButtonDown(1)){
				WorldStorage.getInstance().setBlockAt(t.x+t.side.getX(), t.y+t.side.getY(), t.z+t.side.getZ(), id);
			}
			b1state = Mouse.isButtonDown(1);
			if(!b2state&&Mouse.isButtonDown(0)){
				WorldStorage.getInstance().setBlockAt(t.x, t.y, t.z, 0);
			}
			b2state = Mouse.isButtonDown(0);
		}
	}
	private class VertexBufferObject implements IUpdateable{

		private ArrayList<Renderer> rnders = new ArrayList<Renderer>();
		private int bufid,verts;
		private long size,avalible;
		private boolean marked,init=false;

		public VertexBufferObject(int id, long size){
			this.bufid = id;
			this.size = size;
			this.avalible = size;
		}

		public void addRenderer(Renderer r){
			if(!this.rnders.contains(r)){
				this.rnders.add(r.setUpdateable(this));
				this.markForUpdate();
				this.avalible -= (r.getVerts()*8);
			}
		}

		public void reupload(){
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.bufid);
			if(!init){
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer((int)size), GL15.GL_STREAM_DRAW);
			    init = true;
			}
			verts = 0;
			long offset = 0;
			boolean update = false;
			for(Renderer r : rnders){
				long stride = (r.getVerts()*8);
				if(update||(update=r.isUpdated())){
					if(r.isUpdated()){
						r.updateRenderable();
					}
					r.setUpdated(false);
					GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset<<2, r.getData());
				}
				offset+=stride;
				verts+=r.getVerts();
			}
			System.out.println("Done uploading. Length: "+offset);
			this.avalible = size-(verts*8);
			this.marked = false;

			GL11.glVertexPointer(3, GL11.GL_FLOAT, 8<<2, 0L);
		    GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 8<<2, 3<<2);
		    GL11.glNormalPointer(GL11.GL_FLOAT, 8<<2, 5<<2);
		}

		public void render(){
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.bufid);
			GL11.glVertexPointer(3, GL11.GL_FLOAT, 8<<2, 0L);
		    GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 8<<2, 3<<2);
		    GL11.glNormalPointer(GL11.GL_FLOAT, 8<<2, 5<<2);
			GL11.glDrawArrays(GL11.GL_QUADS, 0, this.verts);
		}

		public boolean hasRoomForRenderer(Renderer r){
			return (r.getVerts()*8)<=this.avalible;
		}

		@Override
		public void markForUpdate() {
			this.marked = true;
		}

		@Override
		public boolean isMarkedForUpdate() {
			return this.marked;
		}


	}
}
