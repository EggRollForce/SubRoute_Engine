package subroute.gui.event;

import subroute.event.MouseEvent;

public class ComponentClicked extends ComponentEvent{

	public int x,y;
	public boolean[] states;

	public ComponentClicked(int x, int y, boolean[] states) {
		super("Clicked");
		this.x = x;
		this.y = y;
		this.states = states;
	}
	public ComponentClicked(MouseEvent e){
		this(e.x,e.y,e.states);
	}

}
