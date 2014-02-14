package subroute.gui.event;

import subroute.gui.component.Button;

public class ButtonEvent extends ComponentEvent{

	Button button;

	public ButtonEvent(Button button) {
		super("Button");
		this.button = button;
	}

	public String getButtonName(){
		return button.name;
	}

	public Button getButton(){
		return this.button;
	}

	@Override
	public String toString(){
		return this.button.toString();
	}

}
