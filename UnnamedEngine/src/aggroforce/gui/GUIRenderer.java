package aggroforce.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import aggroforce.game.Game;
import aggroforce.render.RenderEngine;

public class GUIRenderer {

	private static GUI current;

	public static void renderGUI(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
//		float aspect = (float)Game.instance().getScreenWidth()/(float)Game.instance().getScreenHeight();
		GL11.glOrtho(0, Game.instance().getScreenWidth(), 0, Game.instance().getScreenHeight(), -1, 10);
//		GLU.gluPerspective(45.0f,((float)Game.instance().getScreenWidth()/(float)Game.instance().getScreenHeight()),0.1f,10.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, -5);
		long max = Runtime.getRuntime().maxMemory();
		long free = Runtime.getRuntime().freeMemory();
		long total = Runtime.getRuntime().totalMemory();
		RenderEngine.fontRenderer.drawString("FPS: "+Game.instance().getFps(), 2, Game.instance().getScreenHeight()-20, 2f);
		RenderEngine.fontRenderer.drawString("Mem: Max-"+(max==Long.MAX_VALUE?"unlim.":(int)(max/1024d/1024d))+"MB Free-"+(int)(free/1024d/1024d)+"MB Total-"+(int)(total/1024d/1024d)+"MB", 2, Game.instance().getScreenHeight()-40, 2f);
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

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f,((float)Game.instance().getScreenWidth()/(float)Game.instance().getScreenHeight()),0.1f,10000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}

	public static void setCurrentGUI(GUI gui){
		current = gui;
	}

	public static GUI getCurrentGUI(){
		return current;
	}


}
