package aggroforce.menu;

import aggroforce.event.EventRegistry;
import aggroforce.event.MouseEvent;
import aggroforce.event.listener.IMouseListener;
import aggroforce.gui.GUI;
import aggroforce.gui.GUIRenderer;
import aggroforce.gui.component.Button;
import aggroforce.gui.event.ComponentClicked;

public class MainMenu extends GUI implements IMouseListener{

	public MainMenu(){
		EventRegistry.EVENT_BUS.registerListener(this);
		GUIRenderer.setCurrentGUI(this);
		this.addChild(new Button(this,100,100,100,20));
	}

	@Override
	public void onMouseEvent(MouseEvent evt) {
		this.onEvent(new ComponentClicked(evt));
	}
}
