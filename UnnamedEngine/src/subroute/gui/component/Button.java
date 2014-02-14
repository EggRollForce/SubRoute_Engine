package subroute.gui.component;

import java.io.File;

import org.lwjgl.opengl.GL11;

import subroute.event.EventRegistry;
import subroute.gui.GUIComponent;
import subroute.gui.event.ButtonEvent;
import subroute.gui.event.ComponentClicked;
import subroute.gui.event.GUIEvent;
import subroute.render.RenderEngine;
import subroute.texture.Texture;


public class Button extends GUIComponent {

	private static final Texture tex = Texture.loadTextureFromFile("ButtonOn", new File("resource/gui/ButtonUp.png"));
	public boolean on = false;
	public String name;

	public Button(GUIComponent parent, String name, int xoffset, int yoffset, int width, int height) {
		super(parent, xoffset, yoffset, width, height);
		this.name = name;
	}

	@Override
	public void renderBackground(){
		if(this.on){
			GL11.glColor3f(0f, 1f, 0f);
		}else{
			GL11.glColor3f(1f, 1f, 1f);
		}
		this.drawTexRect(tex, this.x, this.y, this.width, this.height);
		GL11.glTranslated(0, 0, 1);
		RenderEngine.fontRenderer.drawString(name, x+width/10, y+height/3, 2f);
	}

	@Override
	public void onEvent(GUIEvent e){
		if(e instanceof ComponentClicked){
			ComponentClicked evt = (ComponentClicked)e;
			if(this.isWithinComponent(evt.x, evt.y)||!evt.states[0]){
				on = evt.states[0];
				if(on){
					EventRegistry.EVENT_BUS.postEvent(new ButtonEvent(this));
				}
			}
		}
	}

	@Override
	public String toString(){
		return "Button:["+name+","+(on?"on":"off")+",["+super.toString()+"]]";
	}

}
