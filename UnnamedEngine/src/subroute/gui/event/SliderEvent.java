package subroute.gui.event;

import subroute.gui.component.Slider;

public class SliderEvent extends ComponentEvent {

	private Slider slider;

	public SliderEvent(Slider slider) {
		super("Slider");
		this.slider = slider;
	}

	@Override
	public String toString(){
		return this.slider.toString();
	}
}
