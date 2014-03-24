package subroute.audio;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import subroute.render.camera.Camera;

public class AudioEngine {

	private static AudioEngine instance;

	public IntBuffer buf = BufferUtils.createIntBuffer(1);
	public IntBuffer src = BufferUtils.createIntBuffer(1);

	private float[] apos = new float[] {0f,0f,0f};
	private FloatBuffer ori = BufferUtils.createFloatBuffer(6);

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
	public void loop(){
		AL10.alListener3f(AL10.AL_POSITION, -(float)Camera.x, -(float)Camera.y, (float)Camera.z);
		ori.clear();
		ori.put(Camera.getVeiwAT()).put(Camera.getViewUP());
		AL10.alListener(AL10.AL_ORIENTATION, (FloatBuffer) ori.rewind());

	}
	public void playSound(){
		AL10.alSource(src.get(0), AL10.AL_POSITION, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] {0f,(float)Camera.y,0f}).rewind());
		AL10.alListener3f(AL10.AL_POSITION, -(float)Camera.x, -(float)Camera.y, -(float)Camera.z);
		System.out.println("Atempting to play sound");
		AL10.alSourcePlay(src.get(0));
	}
	public void loadSound(){
		AL10.alListenerf(AL10.AL_GAIN, 0.2f);
		AL10.alListener3f(AL10.AL_POSITION, 0,0,0);
		AL10.alListener(AL10.AL_VELOCITY, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] {0f,0f,0f}).rewind());
		AL10.alListener(AL10.AL_ORIENTATION, (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] {0f,0f,-1f,0,1f,0}).rewind());
		AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);
		AL10.alGenBuffers(buf);
		AL10.alGenSources(src);
		AL10.alGetError();
		this.initAudioEngine();
		AudioFileReader reader = new AudioFileReader(new File("resource/sound/route1.wav"));
		int format =reader.getAudioFormat();
		if(format == AL10.AL_FORMAT_MONO16 || format == AL10.AL_FORMAT_MONO8){
			AL10.alBufferData(buf.get(0), reader.getAudioFormat(), reader.getAudioData(), reader.getSampleRate());

		}else{

			System.err.println("Cannot use Stereo audio data!");

		}
		AL10.alSourcei(src.get(0), AL10.AL_BUFFER, buf.get(0));
	}

	public void initAudioEngine(){
		AL10.alSourcef(src.get(0), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(src.get(0), AL10.AL_ROLLOFF_FACTOR, 1f);
		AL10.alSource(src.get(0), AL10.AL_POSITION, (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] {0f,500f,0f}).rewind());
		AL10.alSourcef(src.get(0), AL10.AL_REFERENCE_DISTANCE, 1f);
		AL10.alSourcef(src.get(0), AL10.AL_MAX_DISTANCE, 100f);
		AL10.alSourcei(src.get(0), AL10.AL_LOOPING, AL10.AL_TRUE);
	}
}
