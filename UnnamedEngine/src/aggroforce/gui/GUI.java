package aggroforce.gui;

import aggroforce.event.EventHandler;
import aggroforce.event.EventRegistry;
import aggroforce.event.MouseEvent;
import aggroforce.event.listener.IEventListener;

public class GUI extends GUIComponent implements IEventListener{

	public GUI() {
		super(null);
		EventRegistry.EVENT_BUS.registerListener(this);
	}

	@EventHandler
	public void onEvent(MouseEvent e){
		System.out.println(e);
	}

}
