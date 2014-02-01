package aggroforce.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TileRegister {

	public Icon loadNewIcon(String path){
		System.out.println("Attempting Icon load");
		File f = new File(path);
		if(f.exists()){
			try{
				BufferedImage img = ImageIO.read(f);
				Icon icon = TextureTile.loadImgData(TextureMap.blockMap, img.getWidth(), img.getHeight(), img);
				return icon;
			}catch(IOException e){
				e.printStackTrace();
			}
		}else{
			System.out.println("File does not exist at path: "+path);
		}
		return null;
	}
}
