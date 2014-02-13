package aggroforce.input;

import org.lwjgl.input.Mouse;

import aggroforce.event.EventRegistry;
import aggroforce.event.MouseEvent;
import aggroforce.render.camera.Camera;

public class MouseReader {
	private static boolean[] buttons = new boolean[Mouse.getButtonCount()];

	public MouseReader(){
		Mouse.setGrabbed(false);
	}
	public void grabbedToggle(){
		if(!Mouse.isGrabbed()){
			Mouse.setGrabbed(true);
		}else{
			Mouse.setGrabbed(false);
		}
	}

	int button = 0;


	public void loop(){
		if(Mouse.isGrabbed()){
			Camera.mouseIn(Mouse.getDX(), Mouse.getDY());
		}
		boolean changed = false;
		while(Mouse.next()){
			int b = Mouse.getEventButton();
			if(b!=-1){
				boolean temp;
				if(buttons[b] != (temp=Mouse.getEventButtonState())){
					changed = true;
					buttons[b] = temp;
				}
			}
			EventRegistry.EVENT_BUS.postEvent(new MouseEvent(Mouse.getEventX(),Mouse.getEventY(),buttons,changed));
		}
	}

}
