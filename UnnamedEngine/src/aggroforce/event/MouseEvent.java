package aggroforce.event;

public class MouseEvent extends Event {

	public int x,y,id;

	public MouseEvent(int x, int y, int button) {
		super("Mouse", false);
		this.x = x;
		this.y = y;
		this.id = button;
	}
}
