package aggroforce.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import aggroforce.render.camera.BlockTarget;
import aggroforce.render.camera.Camera;
import aggroforce.texture.TextureMap;
import aggroforce.world.storage.WorldStorage;

public class RenderBlocks {

	private static FloatBuffer data;

	private int bid;
	private int verts = 0;
	private boolean uploaded=false;

	public RenderBlocks(){
		bid = GL15.glGenBuffers();
		if(data==null){
			data = BufferUtils.createFloatBuffer(80000000);
		}
	}

	public boolean firstup = true;
	public void upload(){
		verts=0;
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bid);
		if(firstup){
			firstup = false;
	    	for(Renderer render: WorldStorage.getRenderers()){
				data.put(render.getData());
				verts+=render.getVerts();
			}
	    	data.position(data.capacity());
	    	data.flip();
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STREAM_DRAW);
			GL11.glVertexPointer(3, GL11.GL_FLOAT, 8<<2, 0L);
		    GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 8<<2, 3<<2);
		    GL11.glNormalPointer(GL11.GL_FLOAT, 8<<2, 5<<2);
		}else{
			long offset = 0;
			boolean update = false;
			for(Renderer render : WorldStorage.getRenderers()){
				long stride = (render.getVerts()*8);
				if(update||(update=render.isUpdated())){
					render.setUpdated(false);
					GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset<<2, render.getData());
				}
				offset+=stride;
				verts+=render.getVerts();
			}
		}
	}

	public void render(){
		if(!uploaded||WorldStorage.getInstance().getIsUpdateNeeded()){
			this.upload();
			this.uploaded = true;
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		TextureMap.blockMap.bindTextureMap();
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bid);
		GL11.glDrawArrays(GL11.GL_QUADS, 0, verts);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
	}

	float off = 0.001f;
	boolean bstate = false;
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
			if(/*!bstate&&*/Mouse.isButtonDown(1)){
				WorldStorage.getInstance().setBlockAt(t.x+t.side.getX(), t.y+t.side.getY(), t.z+t.side.getZ(), 7);
			}
			bstate = Mouse.isButtonDown(1);
		}
	}
}
