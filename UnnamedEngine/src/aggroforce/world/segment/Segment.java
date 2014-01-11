package aggroforce.world.segment;

import java.io.File;

import org.lwjgl.opengl.GL11;

import aggroforce.block.Block;
import aggroforce.render.RenderBlocks;
import aggroforce.texture.Texture;
import aggroforce.util.Side;
import aggroforce.world.storage.ISegmentAccess;

public class Segment implements ISegmentAccess{

	public int segx;
	public int segy;

	private int dlist;

	public int[][][] blockStorage = new int[16][16][1024];

	public static Texture test = Texture.loadTextureFromFile("Stone", new File("resource/textures/blocks/terrain.png"));
	public static Texture side = Texture.loadTextureFromFile("Stone", new File("resource/textures/blocks/grass-side.png"));

	public Segment(int x, int y, int[][] heightData){
		this.segx = x;
		this.segy = y;
		this.generateBlocks(heightData);
	}

	@Override
	public int getBlockIdAt(int x, int y, int z){
		int dx = Math.signum(x)==-1?(x%16)+15:(x%16);
		int dz = Math.signum(z)==-1?(z%16)+15:(z%16);
		return this.blockStorage[dx][dz][y];
	}

	public void setupDisplayList(RenderBlocks rb){
		this.dlist = GL11.glGenLists(1);
		GL11.glNewList(dlist, GL11.GL_COMPILE);
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Segment.test.getGLTexID());
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glTranslated(this.segx*16d, 0, this.segy*16d);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Segment.side.getGLTexID());
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				GL11.glBegin(GL11.GL_QUADS);
				for(int k=0;k<1024;k++){
					Block blk = Block.blocks[this.blockStorage[i][j][k]];
					if(blk.renderType()!=-1){
						int x = i+(int)(16*segx*Math.signum(segx));
						int z = j+(int)(16*segy*Math.signum(segy));
						if(blk.shouldRenderSide(this, x, k, z, Side.UP)){
							GL11.glColor4f(0f, 0f, 0f, 0f);
							GL11.glNormal3f(0f, 1f, 0f);
							GL11.glTexCoord2f(1f/16f, 1f/16f);
							GL11.glVertex3d(i+1, k, j+1);
							GL11.glTexCoord2f(0, 1f/16f);
							GL11.glVertex3d(i, k, j+1);
							GL11.glTexCoord2f(0, 0);
							GL11.glVertex3d(i, k, j);
							GL11.glTexCoord2f(1f/16f, 0);
							GL11.glVertex3d(i+1, k, j);
//							rb.addVTN(i+1+(16*segx), k, j+1+(16*segy), 1f/16f, 1f/16f, 0, 1f, 0);
//							rb.addVTN(i+(16*segx), k, j+1+(16*segy), 0, 1f/16f, 0, 1f, 0);
//							rb.addVTN(i+(16*segx), k, j+(16*segy), 0, 0, 0, 1f, 0);
//							rb.addVTN(i+1+(16*segx), k, j+(16*segy), 1f/16f, 0, 0, 1f, 0);
						}
						if(blk.shouldRenderSide(this, x, k, z, Side.SOUTH)){
							GL11.glColor4f(1f, 0f, 0f, 1f);
							GL11.glNormal3f(1f, 0f, 0f);
							GL11.glTexCoord2f(2f/16f, 0);
							GL11.glVertex3d(i, k, j+1);
							GL11.glTexCoord2f(2f/16f, 1f/16f);
							GL11.glVertex3d(i, k-1, j+1);
							GL11.glTexCoord2f(1f/16f, 1f/16f);
							GL11.glVertex3d(i, k-1, j);
							GL11.glTexCoord2f(1f/16f, 0);
							GL11.glVertex3d(i, k, j);
						}
						if(blk.shouldRenderSide(this, x, k, z, Side.NORTH)){
							GL11.glColor4f(0f, 1f, 0f, 1f);
							GL11.glNormal3f(-1f, 0f, 0f);
							GL11.glTexCoord2f(1f/16f, 0);
							GL11.glVertex3d(i+1, k, j+1);
							GL11.glTexCoord2f(2f/16f, 0);
							GL11.glVertex3d(i+1, k, j);
							GL11.glTexCoord2f(2f/16f, 1f/16f);
							GL11.glVertex3d(i+1, k-1, j);
							GL11.glTexCoord2f(1f/16f, 1f/16f);
							GL11.glVertex3d(i+1, k-1, j+1);
						}
						if(blk.shouldRenderSide(this, x, k, z, Side.WEST)){
							GL11.glColor4f(0f, 0f, 0f, 0f);
							GL11.glNormal3f(0f, 0f, -1f);
							GL11.glTexCoord2f(2f/16f, 1f/16f);
							GL11.glVertex3d(i, k-1, j);
							GL11.glTexCoord2f(1f/16f, 1f/16f);
							GL11.glVertex3d(i+1, k-1, j);
							GL11.glTexCoord2f(1f/16f, 0);
							GL11.glVertex3d(i+1, k, j);
							GL11.glTexCoord2f(2f/16f, 0);
							GL11.glVertex3d(i, k, j);
						}
						if(blk.shouldRenderSide(this, x, k, z, Side.EAST)){
							GL11.glColor4f(0f, 0f, 0f, 0f);
							GL11.glNormal3f(0f, 0f, 1f);
							GL11.glTexCoord2f(1f/16f, 0);
							GL11.glVertex3d(i+1, k, j+1);
							GL11.glTexCoord2f(1f/16f, 1f/16f);
							GL11.glVertex3d(i+1, k-1, j+1);
							GL11.glTexCoord2f(2f/16f, 1f/16f);
							GL11.glVertex3d(i, k-1, j+1);
							GL11.glTexCoord2f(2f/16f, 0);
							GL11.glVertex3d(i, k, j+1);
						}
					}
				}
				GL11.glEnd();
			}
		}
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
		GL11.glEndList();
	}
	public void generateBlocks(int[][] hmap){
		for(int i = 0; i<16; i++){
			for(int j = 0; j<16; j++){
				int h = hmap[i][j];
				for(int k = 0; k<1024; k++){
					if(k<h){
						this.blockStorage[i][j][k] = 1;
					}else{
						this.blockStorage[i][j][k] = 0;
					}
				}
			}
		}
	}

	public int getDisplayListID(){
		return this.dlist;
	}

	public void render(){
		GL11.glCallList(this.dlist);
	}
}
