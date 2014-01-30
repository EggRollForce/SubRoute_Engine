package aggroforce.audio;

import org.lwjgl.openal.AL;

public class AudioEngine {

	private static AudioEngine instance;

	public AudioEngine(){
		instance = this;
		try{
			AL.create();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("OpenAL failed to initilize!");
			System.exit(-1);
		}finally{
			System.out.println("OpenAL initialized!");
		}
	}

	public static AudioEngine instance(){
		return instance;
	}
	public void playSound(){
	}
	public void loadSound(){

	}
}
