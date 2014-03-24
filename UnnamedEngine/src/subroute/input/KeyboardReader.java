package subroute.input;

import org.lwjgl.input.Keyboard;

import subroute.render.camera.Camera;


public class KeyboardReader {
	public static KeyboardReader instance;
	public static boolean[] keysts = new boolean[Keyboard.KEYBOARD_SIZE];
	public static char lastChar;
	private boolean toggle = false;

	public KeyboardReader() {
		instance = this;
	}

	public void loop() {
		while(Keyboard.next()){
//			System.out.println(Keyboard.getEventKey());
			if(Keyboard.getEventKeyState()){
				keysts[Keyboard.getEventKey()] = true;
				if(Keyboard.getEventKey()==Keyboard.KEY_ESCAPE){
					Input.mouse.grabbedToggle();
				}
			}else{
				keysts[Keyboard.getEventKey()] = false;
			}
			lastChar = Keyboard.getEventCharacter();
		}
		Camera.resetInput();
		if(keysts[Keyboard.KEY_W]){
			Camera.forward();
		}
		if(keysts[Keyboard.KEY_S]){
			Camera.backward();
		}
		if(keysts[Keyboard.KEY_D]){
			Camera.strafeRight();
		}
		if(keysts[Keyboard.KEY_A]){
			Camera.strafeLeft();
		}
		if(keysts[Keyboard.KEY_SPACE]!=toggle&&keysts[Keyboard.KEY_SPACE]){
			Camera.up();
		}
		toggle = keysts[Keyboard.KEY_SPACE];
		if(keysts[Keyboard.KEY_LCONTROL]){
			Camera.down();
		}
		if(keysts[Keyboard.KEY_LSHIFT]){
			Camera.sprintOn();
		}else{
			Camera.sprintOff();
		}
		Camera.inputDone();
	}
}
