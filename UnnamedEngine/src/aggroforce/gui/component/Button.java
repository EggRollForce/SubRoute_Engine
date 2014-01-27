package aggroforce.gui.component;

import java.io.File;

import org.lwjgl.opengl.GL11;

import aggroforce.gui.GUIComponent;
import aggroforce.gui.event.ComponentClicked;
import aggroforce.gui.event.GUIEvent;
import aggroforce.gui.GUIRenderer;
import aggroforce.render.RenderEngine;
import aggroforce.texture.Texture;

public class Button extends GUIComponent {

	private static final Texture tex = Texture.loadTextureFromFile("ButtonOn", new File("resource/gui/ButtonUp.png"));
	public boolean on = false;

	public Button(GUIComponent parent, int xoffset, int yoffset, int width, int height) {
		super(parent, xoffset, yoffset, width, height);
	}

	@Override
	public void renderBackground(){
		if(this.on){
			GL11.glColor3f(0f, 1f, 0f);
		}else{
			GL11.glColor3f(1f, 1f, 1f);
		}
		this.drawRect(this.x, this.y, this.width, this.height);
	}

	@Override
	public void onEvent(GUIEvent e){
		if(e instanceof ComponentClicked){
			ComponentClicked evt = (ComponentClicked)e;
			if(this.isWithinComponent(evt.x, evt.y)||!evt.states[0]){
				on = evt.states[0];
				if(evt.states[0]){
					GUIRenderer.setCurrentGUI(null);
					RenderEngine.instance.loadWorld();
				}
			}
		}
	}

}
