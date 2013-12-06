package aggroforce.render;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import aggroforce.game.Game;
import aggroforce.gen.noise.NoiseGeneratorPerlin;
import aggroforce.input.KeyboardReader;
import aggroforce.texture.TextureMap;
import aggroforce.world.World;
import aggroforce.world.WorldLoader;
import aggroforce.world.storage.WorldStorage;

public class RenderEngine {
	private float x;
	private World wld;
	private static final long seed = 0;
	double dx = 0;
	double dy = 0;
	public static RenderEngine instance;
	public static TextureMap textureMap;
	public static FontRenderer fontRenderer;
	private static Random rand = new Random(seed);
	private static NoiseGeneratorPerlin ngp = new NoiseGeneratorPerlin(rand,16);
	public RenderEngine(){
		this.genTerrainList(0, 0);
		instance = this;
		fontRenderer = new FontRenderer();
		textureMap = new TextureMap();
		textureMap.loadBaseTextures();
		new GUIRenderer();
		new Camera(0d,-255d,0d);
	}

	private final Vector3f lpos = new Vector3f(0,0,0);
	float time;
	WorldStorage wldstor = new WorldStorage(new WorldLoader(0L));
	public void renderLoop(){


		GL11.glClearColor(0.5f, 0.5f, 0.5f, 1f);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT|GL11.GL_COLOR_BUFFER_BIT);

		GL11.glPushMatrix();

		Camera.camTransform();

		if(KeyboardReader.keysts[Keyboard.KEY_UP]){
		time += 1d;
		}
		if(KeyboardReader.keysts[Keyboard.KEY_DOWN]){
			time -= 1d;
		}
		if(KeyboardReader.keysts[Keyboard.KEY_LEFT]){
			dy += 1d;
			this.regenTerrain();
		}
		if(KeyboardReader.keysts[Keyboard.KEY_RIGHT]){
			dy -= 1d;
			this.regenTerrain();
		}
		if(KeyboardReader.keysts[Keyboard.KEY_R]){
			this.regenTerrain();
		}

		lpos.y=1;
		if(time > 360){
			time=0;
		}else{
			time+=Game.instance().getDelta()/1000f;
		}
		double p = Math.sin(Math.toRadians(time))*1000;
		double p2 = Math.cos(Math.toRadians(time))*1000;
		double p3 = Math.sin(Math.toRadians(time+180))*1000;
		double p4 = Math.cos(Math.toRadians(time+180))*1000;
		FloatBuffer pos = BufferUtils.createFloatBuffer(4).put(new float[]{1f,(float)p3+256,(float)p4,0.2f});
		FloatBuffer pos2 = BufferUtils.createFloatBuffer(4).put(new float[]{1f,(float)p+256,(float)p2,0.2f});
		FloatBuffer gamb = (FloatBuffer) BufferUtils.createFloatBuffer(4).put(new float[]{1f,1f,1f,1f}).flip();
		FloatBuffer amb = (FloatBuffer) BufferUtils.createFloatBuffer(4).put(new float[]{1f,1f,1f,1}).flip();
		FloatBuffer amb2 = (FloatBuffer) BufferUtils.createFloatBuffer(4).put(new float[]{1f,1f,1f,1f}).flip();
		FloatBuffer diff = (FloatBuffer) BufferUtils.createFloatBuffer(4).put(new float[]{0.2f,0.2f,0.2f,0.5f}).flip();
		FloatBuffer diff2 = (FloatBuffer) BufferUtils.createFloatBuffer(4).put(new float[]{1f,1f,1f,0.5f}).flip();
		FloatBuffer spec = (FloatBuffer) BufferUtils.createFloatBuffer(4).put(new float[]{0f,0f,0f,0f}).flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, (FloatBuffer) pos.flip());
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, (FloatBuffer) pos2.flip());
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, amb);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, diff);
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, diff2);
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, amb2);
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, spec);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, spec);
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, gamb);
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 0f);

		FloatBuffer fcolor = (FloatBuffer) BufferUtils.createFloatBuffer(4).put(new float[]{0.5f,0.5f,0.5f,1f}).flip();
		GL11.glFogf(GL11.GL_FOG_DENSITY, 100f);
		GL11.glFog(GL11.GL_FOG_COLOR, fcolor);
		GL11.glFogi(GL11.GL_FOG_END, 90);
		GL11.glFogi(GL11.GL_FOG_START, 80);
		GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
		GL11.glPointSize(10f);
		GL11.glBegin(GL11.GL_POINTS);
		GL11.glColor4f(0f, 0f, 0f, 1f);
		GL11.glVertex3d(0, p+256, p2);
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glVertex3d(0, p3+256, p4);
		GL11.glEnd();

		this.renderTerrain();

		int r = 3;
		//		GL11.glEnable(GL11.GL_FOG);
		GL11.glEnable(GL11.GL_LIGHTING);
		if(time<180){
			GL11.glEnable(GL11.GL_LIGHT1);
			GL11.glDisable(GL11.GL_LIGHT0);
		}else{
			GL11.glEnable(GL11.GL_LIGHT0);
			GL11.glDisable(GL11.GL_LIGHT1);
		}
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glFrontFace(GL11.GL_CW);

		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glPushMatrix();
		//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		//		GL11.glTranslated(0, -1024D, 0);
		wldstor.render();
		//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glPopMatrix();

		GL11.glFrontFace(GL11.GL_CCW);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHT1);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		GUIRenderer.renderGUI();
		GL11.glPopMatrix();

	}

	int dlist;
	int tlist;
	public void genTerrainList(double dx2, double dy2){
		tlist = GL11.glGenLists(1);
		GL11.glNewList(tlist, GL11.GL_COMPILE);
		GL11.glPushMatrix();
		//				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		int width = 16;
		int height = 1024;
		int length = 16;
		double[] grid = ngp.generatePerlinNoise(null, width, height, length, 2, 2, 2, dy, 0, 0);
		GL11.glColor4f(1f, 1f, 1f, 1f);
//		GL11.glTranslated(-500, 0, -500);
		GL11.glPointSize(2f);
		GL11.glColor4f(1f,1f,1f,1f);
		int max = 0;
		int min = 0;
		for(int i = 0; i<width; i++){
			for(int j = 0; j<length; j++){
				GL11.glPushMatrix();
//				GL11.glTranslated(0, 0, j*5);

				GL11.glBegin(GL11.GL_POINTS);
				for(int k = 0; k<height; k++){
					int d = (int)(grid[((i*width*height)+(j*height)+k)]);
//					GL11.glColor4f(d/255f, d/255f, d/255f, 1f);
					if(d<0){
						GL11.glVertex3f(i, j, k);
					}
					if(d>max){
						max = d;
					}
					if(d<min){
						min = d;
					}
				}
				GL11.glEnd();
				GL11.glPopMatrix();
			}
		}
		System.out.println("Length"+grid.length+" Max:"+max+" Min:"+min);
		//		grid = null;
		//		grid = ngp.generatePerlinNoise(width, height, 1.001d, 1.001d, dx+width, dy);
		//		GL11.glColor4f(1f, 1f, 1f, 0.1f);
		//		for(int i = 0; i<width; i++){
		//			GL11.glBegin(GL11.GL_QUAD_STRIP);
		//			for(int j = 0; j<height; j++){
		//				int k = (int)(grid[(i*width)+j]/4d);
		//				if(k<0){
		//					k /= 10;
		//				}
		//				GL11.glVertex3d(width+i, k, j);
		//				GL11.glVertex3d(width+i+1, k, j);
		//				GL11.glVertex3d(width+i, k, j+1);
		//				GL11.glVertex3d(width+i+1, k, j+1);
		//			}
		//			GL11.glEnd();
		//		}
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glPopMatrix();
		GL11.glEndList();
	}
	public void genDisplayList(){
		dlist = GL11.glGenLists(1);
		GL11.glNewList(dlist, GL11.GL_COMPILE);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3f(0,0,1);
		GL11.glVertex3f(  0.5f, 0.5f, 0.5f );
		GL11.glNormal3f(0,0,1);
		GL11.glVertex3f( -0.5f,  0.5f, 0.5f );
		GL11.glNormal3f(0,0,1);
		GL11.glVertex3f( -0.5f, -0.5f, 0.5f );
		GL11.glNormal3f(0,0,1);
		GL11.glVertex3f(  0.5f, -0.5f, 0.5f );
		GL11.glNormal3f(-1, 0, 0);
		GL11.glVertex3f( -0.5f, -0.5f, 0.5f );
		GL11.glNormal3f(-1, 0, 0);
		GL11.glVertex3f( -0.5f,  0.5f, 0.5f );
		GL11.glNormal3f(-1, 0, 0);
		GL11.glVertex3f( -0.5f,  0.5f,  -0.5f );
		GL11.glNormal3f(-1, 0, 0);
		GL11.glVertex3f( -0.5f, -0.5f,  -0.5f );
		GL11.glNormal3f(0,0,-1);
		GL11.glVertex3f( 0.5f, -0.5f,  -0.5f );
		GL11.glNormal3f(0,0,-1);
		GL11.glVertex3f( -0.5f, -0.5f, -0.5f );
		GL11.glNormal3f(0,0,-1);
		GL11.glVertex3f( -0.5f,  0.5f, -0.5f );
		GL11.glNormal3f(0,0,-1);
		GL11.glVertex3f( 0.5f,  0.5f,  -0.5f );
		GL11.glNormal3f(1, 0, 0);
		GL11.glVertex3f(  0.5f,  0.5f,  -0.5f );
		GL11.glNormal3f(1, 0, 0);
		GL11.glVertex3f(  0.5f,  0.5f, 0.5f );
		GL11.glNormal3f(1, 0, 0);
		GL11.glVertex3f( 0.5f,  -0.5f, 0.5f );
		GL11.glNormal3f(1, 0, 0);
		GL11.glVertex3f( 0.5f,  -0.5f,  -0.5f );
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3f(0, -1, 0);
		GL11.glVertex3f( 0.5f, -0.5f,  0.5f );
		GL11.glNormal3f(0, -1, 0);
		GL11.glVertex3f( -0.5f, -0.5f, 0.5f );
		GL11.glNormal3f(0, -1, 0);
		GL11.glVertex3f( -0.5f,  -0.5f, -0.5f );
		GL11.glNormal3f(0, -1, 0);
		GL11.glVertex3f( 0.5f, -0.5f, -0.5f );
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3f(0, 1, 0);
		GL11.glVertex3f(-0.5f, 0.5f, -0.5f);
		GL11.glNormal3f(0, 1, 0);
		GL11.glVertex3f(-0.5f, 0.5f, 0.5f);
		GL11.glNormal3f(0, 1, 0);
		GL11.glVertex3f(0.5f, 0.5f,  0.5f);
		GL11.glNormal3f(0, 1, 0);
		GL11.glVertex3f(0.5f, 0.5f, -0.5f);
		GL11.glEnd();


		GL11.glEndList();
	}

	private void regenList(){
		GL11.glDeleteLists(dlist, 1);
		this.genDisplayList();
	}

	private void regenTerrain(){
		GL11.glDeleteLists(tlist, 1);
		this.genTerrainList(dx,dy);
	}

	public void renderList(){
		GL11.glCallList(dlist);
	}
	public void renderTerrain(){
		GL11.glCallList(tlist);
	}
	public void renderDebugAxis(){
		GL11.glLineWidth(5f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glColor4f(1f, 0f, 0f, 1f);
		GL11.glVertex3i(0, 0, 0);
		GL11.glVertex3i(10, 0, 0);
		GL11.glColor4f(0f, 1f, 0f, 1f);
		GL11.glVertex3i(0, 0, 0);
		GL11.glVertex3i(0, 10, 0);
		GL11.glColor4f(0f, 0f, 1f, 1f);
		GL11.glVertex3i(0, 0, 0);
		GL11.glVertex3i(0, 0, 10);
		GL11.glEnd();
	}
}
