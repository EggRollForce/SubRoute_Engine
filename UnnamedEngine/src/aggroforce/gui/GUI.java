package aggroforce.gui;

import aggroforce.event.EventHandler;
import aggroforce.event.EventRegistry;
import aggroforce.event.MouseEvent;
import aggroforce.event.listener.IEventListener;
import aggroforce.gui.event.ComponentClicked;

public class GUI extends GUIComponent implements IEventListener{

	public GUI() {
		super(null);
		EventRegistry.EVENT_BUS.registerListener(this);
	}

	@EventHandler
	public void onEvent(MouseEvent e){
		if(e.hasChanged){
			this.onEvent(new ComponentClicked(e));
		}
	}

}
