package aggroforce.event;

public abstract class Event {

	private String name;
	private boolean cancelled = false;
	private boolean cancellable;

	public Event(String name, boolean cancellable){
		this.name = name;
		this.cancellable = cancellable;
	}

	public boolean isCancellable(){
		return cancellable;
	}

	public boolean isCancelled(){
		return cancelled;
	}

	public void setCancelled(boolean cancelled){
		this.cancelled = cancelled;
	}

	public String getName(){
		return name;
	}

}
