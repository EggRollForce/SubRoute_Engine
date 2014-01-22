package aggroforce.gui;

import aggroforce.event.MouseEvent;
import aggroforce.gui.event.ComponentClicked;

public class GUI extends GUIComponent{

	public GUI() {
		super(null);
	}

	protected void onScreenClicked(MouseEvent evt){
		for(GUIComponent child : this.getChildren()){
			if(child.isWithinComponent(evt.x, evt.y)){
				child.onEvent(new ComponentClicked(evt.x,evt.y,evt.states));
			}
		}
	}
}
