package aggroforce.menu;

import aggroforce.event.EventHandler;
import aggroforce.event.EventRegistry;
import aggroforce.event.listener.IEventListener;
import aggroforce.gui.GUI;
import aggroforce.gui.GUIRenderer;
import aggroforce.gui.component.Button;
import aggroforce.gui.event.ButtonEvent;
import aggroforce.render.RenderEngine;

public class MainMenu extends GUI implements IEventListener{

	public MainMenu(){
		EventRegistry.EVENT_BUS.registerListener(this);
		GUIRenderer.setCurrentGUI(this);
		this.addChild(new Button(this,"Start",100,100,200,50));
		this.addChild(new Button(this,"Options",100,200,200,50));
	}

	@EventHandler
	public void onButton(ButtonEvent evt){
		System.out.println(evt);
		if(evt.getButtonName().equalsIgnoreCase("start")){
			EventRegistry.EVENT_BUS.unregisterListener(this);
			GUIRenderer.setCurrentGUI(new HeadsUpDisplay());
			RenderEngine.instance.loadWorld();
		}
	}

}
