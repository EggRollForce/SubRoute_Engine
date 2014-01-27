package aggroforce.files.obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import aggroforce.util.Vector;

public class OBJReader {
	private Vector[] varr;

	public OBJReader(File read){
		try {
			BufferedReader br = new BufferedReader(new FileReader(read));
			String str;
			while((str = br.readLine())!=null){
			}
		} catch (Exception e) {
		}
	}
}
