package subroute.gui;

import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import subroute.Game;
import subroute.entity.Entity;
import subroute.profiling.Profiler;
import subroute.render.RenderEngine;
import subroute.render.camera.Camera;


public class GUIRenderer {

	private static GUI current;

	public static void renderGUI(){
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
//		float aspect = (float)Game.instance().getScreenWidth()/(float)Game.instance().getScreenHeight();
		GL11.glOrtho(0, Game.instance().getScreenWidth(), 0, Game.instance().getScreenHeight(), -1, 10);
//		GLU.gluPerspective(45.0f,((float)Game.instance().getScreenWidth()/(float)Game.instance().getScreenHeight()),0.1f,10.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
//		GL11.glDepthMask(false);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

		GL11.glPushMatrix();
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glPointSize(10f);
		GL11.glBegin(GL11.GL_POINTS);
		GL11.glVertex2d(Game.instance().getScreenWidth()/2d, Game.instance().getScreenHeight()/2d);
		GL11.glEnd();
		GL11.glTranslatef(0, 0, -1);
		long max = Runtime.getRuntime().maxMemory();
		long free = Runtime.getRuntime().freeMemory();
		long total = Runtime.getRuntime().totalMemory();
		RenderEngine.fontRenderer.drawString("FPS: "+Game.getFps(), 1, 0, 1f);
		RenderEngine.fontRenderer.drawString("Mem: Max-"+(max==Long.MAX_VALUE?"unlim.":(new DecimalFormat("#.###")).format((max/1024d/1024d/1024d)))+"GB Free-"+(int)(free/1024d/1024d)+"MB Total-"+(new DecimalFormat("#.#####")).format((total/1024d/1024d/1024d))+"GB", 1, 10, 1f);
		RenderEngine.fontRenderer.drawString("Cam Pos:", 1, 50, 1f);
		RenderEngine.fontRenderer.drawString("X:"+Camera.getBoundEntity().getXPos(), 1, 40, 1f);
		RenderEngine.fontRenderer.drawString("Y:"+Camera.getBoundEntity().getYPos(), 1, 30, 1f);
		RenderEngine.fontRenderer.drawString("Z:"+Camera.getBoundEntity().getZPos(), 1, 20, 1f);
		Entity ent = Camera.getBoundEntity();
		RenderEngine.fontRenderer.drawString("Speed:"+(Math.hypot(ent.xPos-ent.lastX, ent.zPos-ent.lastZ))*Game.getFps(), 1, 60, 1f);
		RenderEngine.fontRenderer.drawString("VelX:"+(ent.getXVel()*(Game.getDelta()/1000d))*Game.getFps(), 1, 80, 1f);
		RenderEngine.fontRenderer.drawString("VelZ:"+(ent.getZVel()*(Game.getDelta()/1000d))*Game.getFps(), 1, 70, 1f);
		RenderEngine.fontRenderer.drawString("Delta:"+Game.getDelta(), 1, 90, 1f);
		RenderEngine.fontRenderer.drawString("Time: "+Game.getTimeSinceStart(), 1, 100, 1f);
		RenderEngine.fontRenderer.drawString("Used VBOs: "+RenderEngine.renderBlocks.getUsedVBOS(),1,110,1f);

		for(int i = 0; i < Profiler.getSectionNames().size(); i++){
			String name = (String)Profiler.getSectionNames().toArray()[i];
			RenderEngine.fontRenderer.drawString("Section: "+name+", Time:["+((int)Profiler.getSection(name).getPercentage())+"%, "+(Profiler.getSection(name).elapsed/1000L)/1000D+"]", 1, 120+(10*i), 1f);
		}

		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, -5);
		if(current!=null){
			current.renderBackground();
		}
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, -5);
		if(current!=null){
			current.renderForeground();
		}
		GL11.glPopMatrix();

		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f,((float)Game.instance().getScreenWidth()/(float)Game.instance().getScreenHeight()),0.1f,10000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glPopMatrix();
	}

	public static void setCurrentGUI(GUI gui){
		current = gui;
	}

	public static GUI getCurrentGUI(){
		return current;
	}


}
