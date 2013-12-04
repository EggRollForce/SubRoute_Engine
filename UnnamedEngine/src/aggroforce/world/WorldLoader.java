package aggroforce.world;

import java.io.File;
import java.util.Random;
import aggroforce.gen.noise.NoiseGeneratorPerlin;
import aggroforce.world.segment.Segment;

public class WorldLoader {

	Random rand;

	private NoiseGeneratorPerlin nGen1;

	public WorldLoader(File world){
	}

	public WorldLoader(Long seed){
		this(new Random(seed));
	}

	public WorldLoader(Random rng){
		rand = rng;
		this.initilizeNoiseGens();
	}

	private void initilizeNoiseGens(){
		this.nGen1 = new NoiseGeneratorPerlin(this.rand,16);
	}
	public Segment generateSegment(int x, int y){
		double[] ngrid = this.nGen1.generatePerlinNoise(16, 16, 5, 5, x*16, y*16);
		int[][] hmap = new int[16][16];
		for(int i = 0; i<16; i++){
			for(int j = 0; j<16; j++){
				double k = ngrid[(i*16)+j]/32d;
				if(k<0){
					k /= 5d;
				}
				hmap[i][j] = (int)(256+k);
			}
		}
		return new Segment(x,y,hmap);
	}

}
