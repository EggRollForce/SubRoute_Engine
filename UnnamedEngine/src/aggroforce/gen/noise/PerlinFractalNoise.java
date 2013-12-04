package aggroforce.gen.noise;

import java.util.Random;

public class PerlinFractalNoise extends NoiseGenerator {

	private final int[] perms;
	public double xRand;
	public double yRand;

	public PerlinFractalNoise(){
		this(new Random());
	}

	public PerlinFractalNoise(Random random){
		this.perms = new int[512];
		this.xRand = random.nextDouble()*256D;
		this.yRand = random.nextDouble()*256D;
		this.populatePerms(random);

	}

	private void populatePerms(Random rand){
		for(int i = 0; i < 256; perms[i] = i++){;}
		for(int i = 0; i < 256; i++){
			int j = rand.nextInt(256-i)+i;
			int k = perms[i];
			perms[i]=perms[j];
			perms[j]=k;
			perms[i+256]=perms[i];
		}
	}


	public double[] generateNoiseGrid(int xSize, int ySize, double xScale, double yScale, double xOffset, double yOffset, double noiseDensity){
		return generateNoiseGrid(null,xSize,ySize,xScale,yScale,xOffset,yOffset,noiseDensity);
	}
	public double[] generateNoiseGrid(double[] ngrid, int xSize, int ySize, double xScale, double yScale, double xOffset, double yOffset, double noiseDensity){

		if(ngrid == null){
			ngrid = new double[xSize*ySize];
		}


		int idex = 0;
		int in;
		double dens = 1D/noiseDensity;

		for(int i = 0; i<xSize; i++){
			double sp = i*xScale + this.xRand + xOffset;
			int spi = (int)sp;
			if(sp<spi){
				spi--;
			}
			int x = spi & 255;
			sp -= spi;
			double x2 = sp*sp*sp*(sp*(sp*6D-15D)+10D);
			for(int j = 0; j<ySize; j++){
				double sp2 = j*yScale + this.yRand + yOffset;
				int spi2 = (int)sp2;
				if(sp2<spi2){
					spi2--;
				}
				int y = spi2 & 255;
				sp2 -= spi2;
				double y2 = sp2*sp2*sp2*(sp2*(sp2*6D-15D)+10D);
				int p1 = this.perms[x];
				int p2 = this.perms[p1]+y;
				int p3 = this.perms[x+1];
				int p4 = this.perms[p3]+y;
				double fx = this.interpolate(x2, this.grad(this.perms[p2], sp, sp2), this.grad(this.perms[p4], sp-1D, 0, sp2));
				double fy = this.interpolate(x2, this.grad(this.perms[p2+1], sp, 0, sp2-1D), this.grad(this.perms[p4+1], sp-1D, 0, sp2-1D));
				double fz = this.interpolate(y2, fx, fy);
				in = idex++;
				ngrid[in] += fz*dens;
			}
		}
		return ngrid;
	}

	private double interpolate(double a, double v1, double v2){
		return v1+a*(v2-v1);
	}
	private double grad(int i, double v1, double v2, double v3){
		int i2 = i & 15;
		double d1 = i2<8?v1:v2;
		double d2 = i2<4?v2:(i2!=12&&i2!=14?v3:v1);
		double d3 = ((i2&1)==0?d1:-d1)+((i2&2)==0?d2:-d2);
		return d3;
	}

	private double grad(int i, double v1, double v2){
		int i2 = i&15;
		double d1 = (1-((i2&8)>>3))*v1;
		double d2 = i2<4?0:(i2!=12&&i2!=14?v2:v1);
		double d3 = ((i2&1)==0?d1:-d1)+ ((i2&2)==0?d2:-d2);
		return d3;

	}
}
