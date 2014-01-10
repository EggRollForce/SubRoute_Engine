package aggroforce.gen.noise;

import java.util.Random;

public class PerlinFractalNoise extends NoiseGenerator {

	private final int[] perms;
	public double xRand;
	public double yRand;
	public double zRand;

	public PerlinFractalNoise(){
		this(new Random());
	}

	public PerlinFractalNoise(Random random){
		this.perms = new int[512];
		this.xRand = random.nextDouble()*256D;
		this.yRand = random.nextDouble()*256D;
		this.zRand = random.nextDouble()*256D;
		this.populatePerms(random);

	}

	private void populatePerms(Random rand){
		for(int i = 0; i < 256; System.out.println(perms[i] = i++)){}
		for(int i = 0; i < 256; i++){
			int j = rand.nextInt(256-i)+i;
			int k = perms[i];
			perms[i]=perms[j];
			perms[j]=k;
			perms[i+256]=perms[i];
		}
//		for(int i : perms){
//			System.out.println(i);
//		}
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

	public double[] generateNoiseGrid(int xSize, int ySize, int zSize, double xScale, double yScale, double zScale, double xOffset, double yOffset, double zOffset, double noiseDensity){
		return generateNoiseGrid(null,xSize,ySize,xScale,yScale,xOffset,yOffset,noiseDensity);
	}
	public double[] generateNoiseGrid(double[] ngrid, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale, double xOffset, double yOffset, double zOffset, double noiseDensity){
		if(ngrid == null){
			ngrid = new double[xSize*ySize*zSize];
		}


		int idex = 0;
		int in;
		int n = -1;
		double dens = 1D/noiseDensity;

        for (int i = 0; i < xSize; ++i){
            double sp = xOffset + i * xScale + this.xRand;
            int spi = (int)sp;

            if (sp < spi){
                --spi;
            }

            int x = spi & 255;
            sp -= spi;
            double x2 = sp * sp * sp * (sp * (sp * 6.0D - 15.0D) + 10.0D);

            for (int j = 0; j < zSize; ++j){
                double sp2 = zOffset + j * zScale + this.zRand;
                int spi2 = (int)sp2;

                if (sp2 < spi2){
                    --spi2;
                }

                int z = spi2 & 255;
                sp2 -= spi2;
                double z2 = sp2 * sp2 * sp2 * (sp2 * (sp2 * 6.0D - 15.0D) + 10.0D);

                for (int k = 0; k < ySize; ++k){
                    double sp3 = yOffset + k * yScale + this.yRand;
                    int spi3 = (int)sp3;

                    if (sp3 < spi3){
                        --spi3;
                    }

                    int y = spi3 & 255;
                    sp3 -= spi3;
                    double y2 = sp3 * sp3 * sp3 * (sp3 * (sp3 * 6.0D - 15.0D) + 10.0D);

                    double pd1 = 0, pd2 = 0;
                    if (k == 0 || y != n){
                        n = y;
                        int p1 = this.perms[x] + y;
                        int p2 = this.perms[p1] + z;
                        int p3 = this.perms[p1 + 1] + z;
                        int p4 = this.perms[x + 1] + y;
                        int p5 = this.perms[p4] + z;
                        int p6 = this.perms[p4 + 1] + z;
                        pd1 = this.interpolate(x2, this.grad(this.perms[p2], sp2, sp3, sp2), this.grad(this.perms[p5], sp2 - 1.0D, sp3, sp2));
                        sp = this.interpolate(x2, this.grad(this.perms[p3], sp2, sp3 - 1.0D, sp2), this.grad(this.perms[p6], sp2 - 1.0D, sp3 - 1.0D, sp2));
                        pd2 = this.interpolate(x2, this.grad(this.perms[p2 + 1], sp2, sp3, sp2 - 1.0D), this.grad(this.perms[p5 + 1], sp2 - 1.0D, sp3, sp2 - 1.0D));
                        x2 = this.interpolate(x2, this.grad(this.perms[p3 + 1], sp2, sp3 - 1.0D, sp2 - 1.0D), this.grad(this.perms[p6 + 1], sp2 - 1.0D, sp3 - 1.0D, sp2 - 1.0D));
                    }

                    double px = this.interpolate(y2, pd1, sp);
                    double py = this.interpolate(y2, pd2, x2);
                    double a = this.interpolate(z2, px, py);
                    in = idex++;
                    ngrid[in] += a*dens;
                }
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
