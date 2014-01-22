package aggroforce.event;

public class MouseEvent extends Event {

	public int x,y;
	public boolean[] states;

	public MouseEvent(int x, int y, boolean[] states) {
		super("Mouse", false);
		this.x = x;
		this.y = y;
		this.states = states;
	}

	@Override
	public String toString(){
		String s = "Mouse event[("+x+","+y+"),[";
		for(boolean b : states){
			s+=b;
			s+=",";
		}
		s+="]]";
		return s;
	}
}
