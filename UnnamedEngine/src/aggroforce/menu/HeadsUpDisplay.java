package aggroforce.menu;

import java.io.File;

import org.lwjgl.input.Keyboard;

import aggroforce.game.Game;
import aggroforce.gui.GUI;
import aggroforce.input.KeyboardReader;
import aggroforce.texture.Texture;

public class HeadsUpDisplay extends GUI {

	int hotscreen = -100;
	int hotscreendown;

	public static Texture tex = Texture.loadTextureFromFile("Hotbar", new File("resource/gui/hotbar_nineslot.png"));
	@Override
	public void renderBackground() {
		float size = 1;
		//integer for hotbar increment

		//senses B key pressed and shows the hotbar
		boolean keydown = KeyboardReader.keysts[Keyboard.KEY_B];
		//if b key is pressed, show hotbar
		if(keydown){

			if(hotscreen == 0){
				this.drawTexRect(tex, (int)((Game.instance().getScreenWidth()/2)-(tex.getWidth()*size*0.5)), 0, (int)(tex.getWidth()*size), (int)(tex.getHeight()*size));

			}
			else{
				this.drawTexRect(tex, (int)((Game.instance().getScreenWidth()/2)-(tex.getWidth()*size*0.5)), hotscreen, (int)(tex.getWidth()*size), (int)(tex.getHeight()*size));
				hotscreen+=10;
			}
		}
		else{

			if(hotscreen == -100){
				this.drawTexRect(tex, (int)((Game.instance().getScreenWidth()/2)-(tex.getWidth()*size*0.5)), -100, (int)(tex.getWidth()*size), (int)(tex.getHeight()*size));
				//if at bottom, dont go any farther down!
			}
			else{
			this.drawTexRect(tex, (int)((Game.instance().getScreenWidth()/2)-(tex.getWidth()*size*0.5)), hotscreen, (int)(tex.getWidth()*size), (int)(tex.getHeight()*size));
			hotscreen-=10;
			}
		}
	}


}
