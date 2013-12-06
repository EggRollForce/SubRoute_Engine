package aggroforce.gen.noise;

import java.util.Random;

public class NoiseGeneratorPerlin {

	private final PerlinFractalNoise[] generators;
	private final int octaves;

	public NoiseGeneratorPerlin(Random random, int octaves){
		this.octaves = octaves;
		this.generators = new PerlinFractalNoise[octaves];

		for(int i = 0; i<this.generators.length; i++){
			this.generators[i] = new PerlinFractalNoise(random);
		}
	}

	public double[] generatePerlinNoise(double[] array, int xSize, int ySize, double xScale, double yScale, double xOffset, double yOffset){
		if(array==null){
			array = new double[xSize*ySize];
		}
		double d = 1;
		for(int i = 0; i < this.octaves; i++){
			double dx = xOffset*d*xScale;
			double dy = yOffset*d*yScale;
			this.generators[i].generateNoiseGrid(array, xSize, ySize, xScale*d, yScale*d, dx, dy, d);
			d /= 2;
		}
		return array;
	}
	public double[] generatePerlinNoise(double[] array, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale, double xOffset, double yOffset, double zOffset){
		if(array==null){
			array = new double[xSize*ySize*zSize];
		}
		double d = 1;
		for(int i = 0; i < this.octaves; i++){
			double dx = xOffset*d*xScale;
			double dy = yOffset*d*yScale;
			double dz = zOffset*d*zScale;
			this.generators[i].generateNoiseGrid(array, xSize, ySize, zSize, xScale*d, yScale*d, zScale*d, dx, dy, dz, d);
			d /= 2;
		}
		return array;
	}

	public double[] generatePerlinNoise(int xSize, int ySize, double xScale, double yScale, double xOffset, double yOffset){
		return this.generatePerlinNoise(null, xSize, ySize, xScale, yScale, xOffset, yOffset);
	}
}
