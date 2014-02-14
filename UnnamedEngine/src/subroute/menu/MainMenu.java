package subroute.menu;

import subroute.event.EventHandler;
import subroute.event.EventRegistry;
import subroute.event.listener.IEventListener;
import subroute.gui.GUI;
import subroute.gui.GUIRenderer;
import subroute.gui.component.Button;
import subroute.gui.component.Slider;
import subroute.gui.event.ButtonEvent;
import subroute.gui.event.SliderEvent;
import subroute.render.RenderEngine;

public class MainMenu extends GUI implements IEventListener{

	public MainMenu(){
		EventRegistry.EVENT_BUS.registerListener(this);
		GUIRenderer.setCurrentGUI(this);
		this.addChild(new Button(this,"Start",100,100,200,50));
		this.addChild(new Button(this,"Options",100,200,200,50));
		this.addChild(new Slider(this,100,300,500,20,false,50,0,100));
	}

	@EventHandler
	public void onButton(ButtonEvent evt){
		if(evt!=null&&evt.getButtonName().equalsIgnoreCase("start")){
			EventRegistry.EVENT_BUS.unregisterListener(this);
			GUIRenderer.setCurrentGUI(new HeadsUpDisplay());
			RenderEngine.instance.loadWorld();
		}
	}
	@EventHandler
	public void onSlider(SliderEvent e){
		System.out.println(e);
	}

}
