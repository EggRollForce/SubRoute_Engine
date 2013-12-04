package aggroforce.input;

import org.lwjgl.input.Mouse;
import aggroforce.render.Camera;

public class MouseReader {
	public MouseReader(){
		Mouse.setGrabbed(true);
	}
	public void grabbedToggle(){
		if(!Mouse.isGrabbed()){
			Mouse.setGrabbed(true);
		}else{
			Mouse.setGrabbed(false);
		}
	}
	public void loop(){
		if(Mouse.isGrabbed()){
			Camera.mouseIn(Mouse.getDX(), Mouse.getDY());
		}
	}

}
