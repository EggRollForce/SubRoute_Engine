package aggroforce.texture;

import java.io.File;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

public class TextureMap {
	
	private HashMap<String,Texture> texMap = new HashMap<String,Texture>();
	
	public void loadBaseTextures(){
		loadDebugTexture();
	}
	
	private void loadDebugTexture(){
		Texture debug = Texture.loadTextureFromFile("debug", new File("resource/test/debug.png"));
		texMap.put("debug", debug);
	}
	
	
	public void useDebugTexture(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texMap.get("debug").getGLTexID());
	}
}
