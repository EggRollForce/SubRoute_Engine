package aggroforce.audio;

import java.io.File;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

public class AudioFileReader {

	public ByteBuffer data;
	private AudioFormat format;
	private byte[] adat;
	public AudioFileReader(File file){
		try {
		if(AudioSystem.isFileTypeSupported(AudioSystem.getAudioFileFormat(file).getType())){
				AudioInputStream audio = AudioSystem.getAudioInputStream(file);
				format = audio.getFormat();
				data = BufferUtils.createByteBuffer(audio.available());
				adat = new byte[audio.available()];
				audio.read(adat);
				data.put(adat);
				adat = null;
				data.flip();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ByteBuffer getAudioData(){
		return this.data;
	}

	public int getAudioFormat(){
		if(format.getChannels() == 1){
			if(format.getSampleSizeInBits() == 8){
				return AL10.AL_FORMAT_MONO8;
			}else if(format.getSampleSizeInBits() == 16){
				return AL10.AL_FORMAT_MONO16;
			}
		}else if(format.getChannels() == 2){
			if(format.getSampleSizeInBits() == 8){
				return AL10.AL_FORMAT_STEREO8;
			}else if(format.getSampleSizeInBits() == 16){
				return AL10.AL_FORMAT_STEREO16;
			}
		}
		return -1;
	}
	public int getSampleRate(){
		return (int)this.format.getSampleRate();
	}
}
