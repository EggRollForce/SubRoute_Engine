package aggroforce.render;

import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import aggroforce.game.Game;

public class GUIRenderer {

	public static void renderGUI(){
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		float aspect = (float)Game.instance().getScreenWidth()/(float)Game.instance().getScreenHeight();
		float aspect2 = (float)Game.instance().getScreenHeight()/(float)Game.instance().getScreenWidth();
		GL11.glOrtho(0, Game.instance().getScreenWidth(), Game.instance().getScreenHeight(), 0, -1, 10);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		GL11.glTranslatef(0, 0, 1);
		long max = Runtime.getRuntime().maxMemory();
		long free = Runtime.getRuntime().freeMemory();
		long total = Runtime.getRuntime().totalMemory();
		RenderEngine.fontRenderer.drawString("FPS: "+Game.instance().getFps(), 10, 10, 5f);
		RenderEngine.fontRenderer.drawString("Mem: Max-"+(max==Long.MAX_VALUE?"unlim.":(new DecimalFormat("#.###")).format((max/1024d/1024d/1024d)))+"GB Free-"+(int)(free/1024d/1024d)+"MB Total-"+(new DecimalFormat("#.#####")).format((total/1024d/1024d/1024d))+"GB", 10, 80, 5f);
		GL11.glPopMatrix();

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f,((float)Game.instance().getScreenWidth()/(float)Game.instance().getScreenHeight()),0.1f,10000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}

}
