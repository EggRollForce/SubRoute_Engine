package aggroforce.render;

import java.awt.image.BufferedImage;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import aggroforce.game.Game;
import aggroforce.gen.noise.NoiseGeneratorPerlin;
import aggroforce.input.KeyboardReader;
import aggroforce.texture.Texture;

public class GUIRenderer {

	private static long seed = 0;
	private static Random rand = new Random();
	private static NoiseGeneratorPerlin ngp = new NoiseGeneratorPerlin(rand,10);
	private static Texture noise = Texture.loadBufferedImage("noise", loadPerlinNoise(ngp));


	public static void renderGUI(){
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 100, 100, 0, -1, 10);
//		GLU.gluPerspective(45.0f,((float)Game.instance().getScreenWidth()/(float)Game.instance().getScreenHeight()),-1F, 100f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		GL11.glTranslatef(0, 0, 1);
		long max = Runtime.getRuntime().maxMemory();
		long free = Runtime.getRuntime().freeMemory();
		long total = Runtime.getRuntime().totalMemory();
		RenderEngine.fontRenderer.drawString("FPS: "+Game.instance().getFps(), -1, -1, 1f);
		RenderEngine.fontRenderer.drawString("Mem: Max-"+(max==Long.MAX_VALUE?"unlim.":(int)(max/1024d/1024d))+"MB Free-"+(int)(free/1024d/1024d)+"MB Total-"+(int)(total/1024d/1024d)+"MB", -800, -700, 5f);

		//		GL11.glEnable(GL11.GL_TEXTURE_2D);
		//		GL11.glEnable(GL11.GL_BLEND);
		//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, noise.getGLTexID());
		//		GL11.glBegin(GL11.GL_QUADS);
		//		GL11.glColor4f(1f, 1f, 1f,1f);
		//		GL11.glTexCoord2f(0, 0);
		//		GL11.glVertex2f(-500, -500);
		//		GL11.glTexCoord2f(1, 0);
		//		GL11.glVertex2f(500, -500);
		//		GL11.glTexCoord2f(1, 1);
		//		GL11.glVertex2f(500, 500);
		//		GL11.glTexCoord2f(0, 1);
		//		GL11.glVertex2f(-500, 500);
		//		GL11.glEnd();
		//		GL11.glDisable(GL11.GL_TEXTURE_2D);

		//		GL11.glBegin(GL11.GL_QUADS);
		//		GL11.glColor4f(0.2f, 0.2f, 0.2f,0.5f);
		//		GL11.glVertex2d(-Display.getWidth(),-Display.getHeight());
		//		GL11.glVertex2d(-Display.getWidth(), Display.getHeight());
		//		GL11.glVertex2d(Display.getWidth(), Display.getHeight());
		//		GL11.glVertex2d(Display.getWidth(), -Display.getHeight());
		//		GL11.glEnd();
		GL11.glPopMatrix();

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f,((float)Game.instance().getScreenWidth()/(float)Game.instance().getScreenHeight()),0.1f,10000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}

	static int offsetx = 0;
	static int offsety = 0;

	private static BufferedImage loadPerlinNoise(NoiseGeneratorPerlin ngp){
		if(KeyboardReader.keysts[Keyboard.KEY_UP]){
			offsetx += 1;
		}
		if(KeyboardReader.keysts[Keyboard.KEY_DOWN]){
			offsetx -= 1;
		}
		if(KeyboardReader.keysts[Keyboard.KEY_LEFT]){
			offsety += 1;
		}
		if(KeyboardReader.keysts[Keyboard.KEY_RIGHT]){
			offsety -= 1;
		}
		double[] grid = ngp.generatePerlinNoise(1024, 1024, 1, 1, 0, 0);
		BufferedImage img = new BufferedImage(1024,1024,BufferedImage.TYPE_INT_ARGB);
		double max = 0;
		double min = 0;
		for(int i = 0; i<grid.length; i++){
			double d = grid[i];
			if(Math.abs(d)>max){
				max = Math.abs(d);
			}
			if(d<min){
				min = d;
			}
		}
		for(int i = 0; i<img.getWidth(); i++){
			for(int j = 0; j<img.getHeight(); j++){
				try{
					int alpha = 127+(int)(((grid[(i*1024)+j])/max)*127);
					if(alpha>255){
						System.err.println("Alpha "+alpha+" exceeds 255 @ ("+i+","+j+")");
					}
					//					System.out.println(grid[(i*256)+j]);
					img.setRGB(j, i, getColor(255, 255, 255, alpha));
				}catch(Exception e){
					System.err.println("Coord ("+i+","+j+") is not in bounds");
				}
			}
		}
		System.out.println("Max: "+max+" Min: "+min);
		return img;
	}

	public static void reloadNoise(){
		noise.deleteTexture();
		noise = Texture.loadBufferedImage("noise", loadPerlinNoise(ngp));
	}
	private static double[] recursiveGenerate(int octave){
		double dens = 1;
		double[] grid = null;
		for(int i = 0; i < octave; i++){
			//			grid = ngp.generateNoiseGrid(grid, 256, 256, 1*dens, 1*dens, 0, 0, dens);
			dens /= 2D;
		}
		return grid;
	}
	private static BufferedImage generateNoiseImage(BufferedImage img, int radius){
		if(rand==null){
			rand = new Random();
		}
		if(img==null){
			img = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
		}

		double[][] grid = new double[256/radius][256/radius];

		for(int px = 0; px < 256/radius; px++){
			for(int py = 0; py < 256/radius;py++){
				grid[px][py] = rand.nextInt(100)/100d;
			}
		}
		for(int px = 0; px < 256/radius-1; px++){
			for(int py = 0; py < 256/radius-1;py++){
				for(int x = 0; x<radius;x++){
					for(int y = 0; y<radius;y++){
						int alpha = 255;
						int mul = 1;
						double c1 = (x/(double)radius)*(y/(double)radius)*grid[px+1][py+1]*mul;
						double c2 = (1-(x/(double)radius))*(y/(double)radius)*grid[px][py+1]*mul;
						double c3 = (1-(x/(double)radius))*(1-(y/(double)radius))*grid[px][py]*mul;
						double c4 = (x/(double)radius)*(1-(y/(double)radius))*grid[px+1][py]*mul;
						double col = (c1+c2+c3+c4)/4d;
						alpha = (int) (255*col)*2;
						int a = ((img.getRGB(px*radius+x, py*radius+y)>>24)&255);
						img.setRGB(px*radius+x, py*radius+y, getColor(255,(int)((alpha+a)/2d), 255, 255));
					}
				}
			}
		}

		return img;
	}

	private static int getColor(int r, int g, int b, int a){
		return r<<16|g<<8|b|a<<24;
	}

}
