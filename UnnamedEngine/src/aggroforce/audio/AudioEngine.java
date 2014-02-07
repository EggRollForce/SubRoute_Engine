package aggroforce.audio;

import java.io.File;
import java.nio.FloatBuffer;
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
			this.loadSound();
			this.initAudioEngine();
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
		System.out.println("Atempting to play sound");
		AL10.alSourcePlay(src.get(0));
	}
	public void loadSound(){
		AudioFileReader reader = new AudioFileReader(new File("resource/sound/test.wav"));
		AL10.alBufferData(buf.get(0), reader.getAudioFormat(), reader.getAudioData(), reader.getSampleRate());
	}

	public void initAudioEngine(){
		AL10.alSourcei(src.get(0), AL10.AL_BUFFER, buf.get(0));
		AL10.alSourcef(src.get(0), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(src.get(0), AL10.AL_GAIN, 1.0f);
		AL10.alSource(src.get(0), AL10.AL_POSITION, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] {0f,0f,0f}).rewind());
		AL10.alSource(src.get(0), AL10.AL_VELOCITY, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] {0f,0f,0f}).rewind());
		AL10.alSourcei(src.get(0), AL10.AL_LOOPING, AL10.AL_TRUE);

		AL10.alListener(AL10.AL_POSITION, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] {0f,0f,0f}).rewind());
		AL10.alListener(AL10.AL_VELOCITY, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] {0f,0f,0f}).rewind());
		AL10.alListener(AL10.AL_ORIENTATION, (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] {0f,0f,-1f,0,1f,0}).rewind());

	}
}
