package aggroforce.gui.event;

import aggroforce.gui.component.Slider;

public class SliderEvent extends ComponentEvent {

	private double value,max,min;

	public SliderEvent(Slider slider) {
		super("Slider");

	}

}
