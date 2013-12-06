package aggroforce.world.segment;

import java.io.File;
import org.lwjgl.opengl.GL11;

import aggroforce.texture.Texture;

public class Segment {

	public int segx;
	public int segy;

	private int dlist;

	public int[][][] blockStorage = new int[16][16][1024];

	public static Texture test = Texture.loadTextureFromFile("Stone", new File("resource/textures/blocks/grass-top.png"));
	public static Texture side = Texture.loadTextureFromFile("Stone", new File("resource/textures/blocks/grass-side.png"));

	public Segment(int x, int y, int[][] heightData){
		this.segx = x;
		this.segy = y;
		this.generateBlocks(heightData);
		this.setupDisplayList();

	}

	public int getBlockIdAt(int x, int y, int z){
		int cx = Math.min(x, 15);
		int cy = Math.min(y, 1023);
		int cz = Math.min(z, 15);
		return this.blockStorage[cx][cz][cy];
	}

	public void setupDisplayList(){
		this.dlist = GL11.glGenLists(1);
		GL11.glNewList(dlist, GL11.GL_COMPILE);
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Segment.test.getGLTexID());
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glTranslated(this.segx*16d, 0, this.segy*16d);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Segment.side.getGLTexID());
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				int m = 0;
				GL11.glBegin(GL11.GL_QUADS);
				for(int k=0;k<1024;k++){
					if(this.blockStorage[i][j][k]==1){
						if(this.blockStorage[i][j][k+1]==0){
//							GL11.glBindTexture(GL11.GL_TEXTURE_2D, Segment.test.getGLTexID());
							GL11.glColor4f(0f, 0f, 0f, 0f);
							GL11.glNormal3f(0f, 1f, 0f);
							GL11.glTexCoord2f(1, 1);
							GL11.glVertex3d(i+1, k, j+1);
							GL11.glTexCoord2f(0, 1);
							GL11.glVertex3d(i, k, j+1);
							GL11.glTexCoord2f(0, 0);
							GL11.glVertex3d(i, k, j);
							GL11.glTexCoord2f(1, 0);
							GL11.glVertex3d(i+1, k, j);
						}
						if(!(i==0)&&this.blockStorage[i-1][j][k]==0){
//							GL11.glBindTexture(GL11.GL_TEXTURE_2D, Segment.side.getGLTexID());
							GL11.glColor4f(1f, 0f, 0f, 1f);
							GL11.glNormal3f(1f, 0f, 0f);
							GL11.glTexCoord2f(1, 0);
							GL11.glVertex3d(i, k, j+1);
							GL11.glTexCoord2f(1, 1);
							GL11.glVertex3d(i, k-1, j+1);
							GL11.glTexCoord2f(0, 1);
							GL11.glVertex3d(i, k-1, j);
							GL11.glTexCoord2f(0, 0);
							GL11.glVertex3d(i, k, j);
						}else if(i==0){}
						if(!(i>=15)&&this.blockStorage[i+1][j][k]==0){
//							GL11.glBindTexture(GL11.GL_TEXTURE_2D, Segment.side.getGLTexID());
							GL11.glColor4f(0f, 1f, 0f, 1f);
							GL11.glNormal3f(-1f, 0f, 0f);
							GL11.glTexCoord2f(0, 0);
							GL11.glVertex3d(i+1, k, j+1);
							GL11.glTexCoord2f(1, 0);
							GL11.glVertex3d(i+1, k, j);
							GL11.glTexCoord2f(1, 1);
							GL11.glVertex3d(i+1, k-1, j);
							GL11.glTexCoord2f(0, 1);
							GL11.glVertex3d(i+1, k-1, j+1);
						}else if(i>=15){}
						if(!(j==0)&&this.blockStorage[i][j-1][k]==0){
//							GL11.glBindTexture(GL11.GL_TEXTURE_2D, Segment.side.getGLTexID());
							GL11.glColor4f(0f, 0f, 0f, 0f);
							GL11.glNormal3f(0f, 0f, -1f);
							GL11.glTexCoord2f(1, 1);
							GL11.glVertex3d(i, k-1, j);
							GL11.glTexCoord2f(0, 1);
							GL11.glVertex3d(i+1, k-1, j);
							GL11.glTexCoord2f(0, 0);
							GL11.glVertex3d(i+1, k, j);
							GL11.glTexCoord2f(1, 0);
							GL11.glVertex3d(i, k, j);
						}else if(j==0){};
						if(!(j>=15)&&this.blockStorage[i][j+1][k]==0){
//							GL11.glBindTexture(GL11.GL_TEXTURE_2D, Segment.side.getGLTexID());
							GL11.glColor4f(0f, 0f, 0f, 0f);
							GL11.glNormal3f(0f, 0f, 1f);
							GL11.glTexCoord2f(0, 0);
							GL11.glVertex3d(i+1, k, j+1);
							GL11.glTexCoord2f(0, 1);
							GL11.glVertex3d(i+1, k-1, j+1);
							GL11.glTexCoord2f(1, 1);
							GL11.glVertex3d(i, k-1, j+1);
							GL11.glTexCoord2f(1, 0);
							GL11.glVertex3d(i, k, j+1);
						}else if(j>=15){};
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
