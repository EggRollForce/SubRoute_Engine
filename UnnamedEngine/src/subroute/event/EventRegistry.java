package subroute.event;

import java.lang.reflect.Method;
import java.util.ArrayList;

import subroute.event.listener.IEventListener;


public class EventRegistry {

	public static final EventRegistry EVENT_BUS = new EventRegistry();

	private ArrayList<IEventListener> listeners = new ArrayList<IEventListener>();

	public void registerListener(IEventListener listener){
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}else{
			System.out.println("Listener already registered!");
		}
	}

	public void unregisterListener(IEventListener listener){
		if(!listeners.remove(listener)){
			System.err.println("Listener not registered!");
		}
	}

	public void postEvent(Event e){
		fireEvent(e);
	}

	@SuppressWarnings("rawtypes")
	private synchronized void fireEvent(Event e){
		for(IEventListener listener : listeners){
			Method[] func = listener.getClass().getMethods();
			for(Method m : func){
				if(m.isAnnotationPresent(EventHandler.class)){
					Class[] cs = m.getParameterTypes();
					if(cs.length > 0){
						Object[] objs = new Object[cs.length];
						int index = 0;
						for(Class c : cs){
							if(Event.class.isAssignableFrom(c)&&c.isInstance(e)){
								objs[index++] = c.cast(e);
							}
						}
						if(objs[0]!=null){
							try{
								m.invoke(listener, objs);
							}catch(Exception ex){
								ex.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}
