package aggroforce.gui.event;

public class ComponentClicked extends ComponentEvent{

	public int x,y,button;

	public ComponentClicked(int x, int y, int button) {
		super("Clicked");
		this.x = x;
		this.y = y;
		this.button = button;
	}

}
