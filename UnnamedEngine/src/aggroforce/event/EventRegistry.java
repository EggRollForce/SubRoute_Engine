package aggroforce.event;

import java.lang.reflect.Method;
import java.util.ArrayList;

import aggroforce.event.listener.IEventListener;

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
			Method[] func = listener.getClass().getMethods();
			for(Method m : func){
				if(m.isAnnotationPresent(EventHandler.class)){
					for(Class c : m.getParameterTypes()){
						if(Event.class.isAssignableFrom(c)){
							try {
								m.invoke(listener,c.cast(e));
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}
