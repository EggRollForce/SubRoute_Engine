package aggroforce.event;

import java.util.ArrayList;

import aggroforce.event.listener.IEventListener;
import aggroforce.event.listener.IMouseListener;

public class EventRegistry {

	public static final EventRegistry EVENT_BUS = new EventRegistry();

	private ArrayList<IEventListener> listeners = new ArrayList<IEventListener>();

	public void registerListener(IEventListener listener, String... types){
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}

	public void postEvent(Event e){
		switch(e.getName().toLowerCase()){
			case "mouse":
				fireEvent(e);
		}
	}

	private void fireEvent(Event e){
		for(IEventListener listener : listeners){
			if(listener instanceof IMouseListener){
				if(e instanceof MouseEvent){
					((IMouseListener) listener).onMouseEvent((MouseEvent) e);
				}
			}
		}
	}
}
