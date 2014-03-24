package subroute.event.tick;

import subroute.event.Event;

public class Tick extends Event {

	private TickType type;

	public Tick(TickType type) {
		super("Tick",false);
		this.type = type;
	}

	public TickType getType(){
		return this.type;
	}
}
