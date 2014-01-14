package aggroforce.gui;

import java.util.ArrayList;

public abstract class GUIComponent {

	private GUIComponent parent;
	private ArrayList<GUIComponent> children;

	public GUIComponent(GUIComponent parent){
		this.setParent(parent);
	}

	public void setParent(GUIComponent parent){
		this.parent = parent;
		parent.addChild(this);
	}
	public final GUIComponent getParent(){
		return this.parent;
	}

	public void renderForeground(){
		for(GUIComponent child : this.getChildren()){
			child.renderForeground();
		}
	}
	public void renderBackground(){
		for(GUIComponent child : this.getChildren()){
			child.renderForeground();
		}
	}

	protected final void sendEvent(){
		if(this.parent!=null){
			parent.sendEvent();
		}
	}

	public ArrayList<GUIComponent> getChildren(){
		return this.children;
	}

	public void addChild(GUIComponent component){
		if(!this.children.contains(component)){
			this.children.add(component);
		}
	}
}
