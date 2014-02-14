package aggroforce.menu;

import aggroforce.event.EventHandler;
import aggroforce.event.EventRegistry;
import aggroforce.event.listener.IEventListener;
import aggroforce.gui.GUI;
import aggroforce.gui.GUIRenderer;
import aggroforce.gui.component.Button;
import aggroforce.gui.component.Slider;
import aggroforce.gui.event.ButtonEvent;
import aggroforce.gui.event.SliderEvent;
import aggroforce.render.RenderEngine;

public class MainMenu extends GUI implements IEventListener{

	public MainMenu(){
		EventRegistry.EVENT_BUS.registerListener(this);
		GUIRenderer.setCurrentGUI(this);
		this.addChild(new Button(this,"Start",100,100,200,50));
		this.addChild(new Button(this,"Options",100,200,200,50));
		this.addChild(new Slider(this,100,300,500,20,false,0,0,100));
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
