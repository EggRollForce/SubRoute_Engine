package aggroforce.phys.util;

public class AABB {

	double x,y,z,w,l,h;

	//Create a new AxisAlignedBoundingBox
	public AABB(double x, double y, double z, double width, double length, double height){
		this.x = x;this.y = y;this.z = z;this.w = width;this.l = length;this.h = height;
	}

	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public double getZ(){
		return z;
	}
	public double getWidth(){
		return w;
	}
	public double getHeight(){
		return h;
	}
	public double getLength(){
		return l;
	}

}
