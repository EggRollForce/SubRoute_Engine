package aggroforce.gui;

public abstract class GUIComponent {

	private GUIComponent parent;

	public GUIComponent(GUIComponent parent){
		this.setParent(parent);
	}

	public void setParent(GUIComponent parent){
		this.parent = parent;
	}
	public final GUIComponent getParent(){
		return this.parent;
	}

	public abstract void renderComponent();

	protected final void sendEvent(){
		if(this.parent!=null){
			parent.sendEvent();
		}
	}
}
