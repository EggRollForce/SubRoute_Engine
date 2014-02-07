package aggroforce.menu;

import java.io.File;

import aggroforce.game.Game;
import aggroforce.gui.GUI;
import aggroforce.texture.Texture;

public class HeadsUpDisplay extends GUI {

	public static Texture tex = Texture.loadTextureFromFile("Hotbar", new File("resource/gui/hotbar_nineslot.png"));
	@Override
	public void renderBackground() {
		float size = 1;
		this.drawTexRect(tex, (int)((Game.instance().getScreenWidth()/2)-(tex.getWidth()*size*0.5)), 0, (int)(tex.getWidth()*size), (int)(tex.getHeight()*size));
	}


}
