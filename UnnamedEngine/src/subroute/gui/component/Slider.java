package subroute.gui.component;

import org.lwjgl.opengl.GL11;

import subroute.event.EventRegistry;
import subroute.gui.GUIComponent;
import subroute.gui.event.ComponentClicked;
import subroute.gui.event.GUIEvent;
import subroute.gui.event.SliderEvent;


public class Slider extends GUIComponent {

	private double sval,eval,val;
	private boolean vert;
	public Slider(GUIComponent parent,int xoffset, int yoffset, int width, int height, boolean vertical, double startval, double minval, double maxval) {
		super(parent, xoffset, yoffset, width, height);
		vert = vertical;
		sval = minval;
		eval = maxval;
		val = startval;

	}

	@Override
	public void onEvent(GUIEvent e) {
		if(e instanceof ComponentClicked){
		ComponentClicked evt = (ComponentClicked)e;
			if(this.isWithinComponent(evt.x, evt.y)){
				if(evt.states[0]){
					System.out.println(evt.x+","+evt.y);
					if(vert){
						this.val = (((evt.y-y)/(double)height)*(eval-sval));
					}else{
						this.val = (((evt.x-x)/(double)width)*(eval-sval));
					}
					EventRegistry.EVENT_BUS.postEvent(new SliderEvent(this));
				}
			}
		}
	}

	public double getValue(){
		return val;
	}
	public double getMaxVal(){
		return eval;
	}
	public double getMinVal(){
		return sval;
	}

	@Override
	public void renderBackground(){
		GL11.glColor4f(0.5f, 0.5f, 1f, 0.5f);
		this.drawRect(x, y, width, height);
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glTranslated(0, 0, 0.1);
		if(vert){
			this.drawRect(x, (int)(y+((val/(eval-sval))*height)), width, 6);
		}else{
			this.drawRect((int)(x+((val/(eval-sval))*width)), y, 6, height);

		}
	}

	@Override
	public String toString(){
		return "Slider:[Val:"+val+","+super.toString()+"]";
	}

}
