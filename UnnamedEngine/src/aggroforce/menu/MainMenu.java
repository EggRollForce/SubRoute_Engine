package aggroforce.menu;

import aggroforce.event.EventHandler;
import aggroforce.event.EventRegistry;
import aggroforce.event.MouseEvent;
import aggroforce.event.listener.IEventListener;
import aggroforce.gui.GUI;
import aggroforce.gui.GUIRenderer;
import aggroforce.gui.component.Button;
import aggroforce.gui.event.ComponentClicked;

public class MainMenu extends GUI implements IEventListener{

	public MainMenu(){
		EventRegistry.EVENT_BUS.registerListener(this);
		GUIRenderer.setCurrentGUI(this);
		this.addChild(new Button(this,"Start",100,100,200,50));
		this.addChild(new Button(this,"Options",100,200,200,50));
	}

	@EventHandler
	public void onMouseEvent(MouseEvent evt) {
		this.onEvent(new ComponentClicked(evt));
	}
}
