package subroute.gui;

import subroute.event.EventHandler;
import subroute.event.EventRegistry;
import subroute.event.MouseEvent;
import subroute.event.listener.IEventListener;
import subroute.gui.event.ComponentClicked;

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
