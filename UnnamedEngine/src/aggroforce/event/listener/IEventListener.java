package aggroforce.event.listener;

import aggroforce.event.Event;

public interface IEventListener {
	public final String type = "all";
	public void onEvent(Event evt);

}
