package aggroforce.audio;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

public class AudioEngine {

	private static AudioEngine instance;

	public IntBuffer buf = BufferUtils.createIntBuffer(1);
	public IntBuffer src = BufferUtils.createIntBuffer(1);

	public AudioEngine(){
		instance = this;
		try{
			AL.create();
			AL10.alGenBuffers(buf);
			AL10.alGenSources(src);
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
