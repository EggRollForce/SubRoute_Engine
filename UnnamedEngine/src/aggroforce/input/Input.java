package aggroforce.input;

public class Input {
	public static KeyboardReader keyboard;
	public static MouseReader mouse;
	public Input(){
		keyboard = new KeyboardReader();
		mouse = new MouseReader();
	}
	public static void checkEvents(){
		keyboard.loop();
		mouse.loop();
	}

}
