package aggroforce.gui;

import java.util.ArrayList;

import aggroforce.gui.event.GUIEvent;

public abstract class GUIComponent {

	private GUIComponent parent;
	private ArrayList<GUIComponent> children;
	private int x,y,width,height;

	public GUIComponent(GUIComponent parent){
		this.setParent(parent);
		this.x = parent.x;
		this.y = parent.y;
		this.width = parent.width;
		this.height = parent.height;
	}

	public GUIComponent(GUIComponent parent, int xoffset, int yoffset, int width, int height){
		this.setParent(parent);
		this.x = xoffset+parent.x;
		this.y = yoffset+parent.y;
		this.width = width;
		this.height = height;
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
			child.renderBackground();
		}
	}

	public void onEvent(GUIEvent evt){
		for(GUIComponent child : children){
			child.onEvent(evt);
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

	protected boolean isWithinComponent(int x, int y){
		if(this.parent==null){
			return true;
		}
		if(x>=this.x&&y>=this.y&&x<=this.x+this.width&&y<=this.y+this.height){
			return true;
		}
		return false;
	}
}
