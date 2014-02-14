package aggroforce.event.tick;

import aggroforce.event.Event;

public class Tick extends Event {

	private TickType type;

	public Tick(TickType type) {
		super("Tick",false);
		this.type = type;
	}
}
