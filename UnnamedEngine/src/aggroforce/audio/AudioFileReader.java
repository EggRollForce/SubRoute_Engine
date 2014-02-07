package aggroforce.audio;

import java.io.File;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.lwjgl.BufferUtils;

public class AudioFileReader {

	public ByteBuffer data;
	public AudioFileReader(File file){
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(file);
			data = BufferUtils.createByteBuffer((int)file.length());
			int dat;
			while((dat = audio.read())!=-1){
				data.put((byte)dat);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
