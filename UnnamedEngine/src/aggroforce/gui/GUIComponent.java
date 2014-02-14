package aggroforce.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import aggroforce.gui.event.GUIEvent;
import aggroforce.texture.Texture;

public abstract class GUIComponent{

	private GUIComponent parent;
	private ArrayList<GUIComponent> children = new ArrayList<GUIComponent>();
	protected int x,y,width,height;

	public GUIComponent(GUIComponent parent){
		if(parent!=null){
			this.setParent(parent);
			this.x = parent.x;
			this.y = parent.y;
			this.width = parent.width;
			this.height = parent.height;
		}
	}

	public GUIComponent(GUIComponent parent, int xoffset, int yoffset, int width, int height){
		if(parent!=null){
			this.setParent(parent);
			this.x = xoffset+parent.x;
			this.y = yoffset+parent.y;
			this.width = width;
			this.height = height;
		}
	}

	public void setParent(GUIComponent parent){
		this.parent = parent;
		if(parent!=null){
			parent.addChild(this);
		}
	}
	public final GUIComponent getParent(){
		return this.parent;
	}

	public void renderForeground(){
		if(this.children==null){return;}
		for(GUIComponent child : this.getChildren()){
			child.renderForeground();
		}
	}
	public void renderBackground(){
		if(this.children==null){return;}
		for(GUIComponent child : this.getChildren()){
			child.renderBackground();
		}
	}

	public void onEvent(GUIEvent evt){
		if(this.children==null){return;}
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

	protected void drawRect(int x, int y, int w, int h){
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x+w, y);
		GL11.glVertex2f(x+w, y+h);
		GL11.glVertex2f(x, y+h);
		GL11.glEnd();
	}

	protected void drawTexRect(Texture tex, int x, int y, int w, int h){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getGLTexID());
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(x, y);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(x+w, y);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(x+w, y+h);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x, y+h);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	public void setPosition(int x, int y){
		this.translate(x-this.x,y-this.y);
	}

	public void translate(int x, int y){
		this.x += x;
		this.y += y;
		for(GUIComponent child : children){
			child.translate(x, y);
		}
	}
	public void setSize(int w, int h){
		this.width = w;
		this.height = h;
	}
	@Override
	public String toString(){
		return "Component:["+x+","+y+","+width+","+height+"]";
	}
}
