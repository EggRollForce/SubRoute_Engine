package subroute.phys.util;

import org.lwjgl.opengl.GL11;

public class AABB {

	public enum Alignment{
		ACTUAL_COORDS,
		BOTTOM_CENTER,
		ABSOLUTE_CENTER
	}
	private Alignment align;
	private double x,y,z,w,l,h;

	//Create a new AxisAlignedBoundingBox
	public AABB(Alignment a, double x, double y, double z, double width, double height, double length){
		align = a;
		this.w = width;
		this.l = length;
		this.h = height;
		switch(align){
		case ACTUAL_COORDS:
			this.x = x;
			this.y = y;
			this.z = z;
			break;
		case BOTTOM_CENTER:
			this.x = x-(w/2d);
			this.y = y;
			this.z = z-(l/2d);
			break;
		case ABSOLUTE_CENTER:
			this.x = x-(w/2d);
			this.y = y-(h/2d);
			this.z = z-(l/2d);
			break;
		}
	}

	public AABB(Alignment a, double width, double height, double length){
		this(a,0,0,0,width,height,length);
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
	public Alignment getAlignment(){
		return align;
	}

	public static boolean intersects(AABB bb1, AABB bb2){
		return bb1.testX(bb2) || bb1.testY(bb2) || bb1.testZ(bb2);
	}

	private boolean testX(AABB bb){
		double d = Math.min(Math.max(this.x, bb.x),this.x+this.w);
		double d2 = Math.min(Math.max(this.x, bb.x+bb.w),this.x+this.w);
		return d == bb.x || (bb.x+bb.w) == d2;
	}
	private boolean testY(AABB bb){
		double d = Math.min(Math.max(this.y, bb.y),this.y+this.h);
		double d2 = Math.min(Math.max(this.y, bb.y+bb.h),this.y+this.h);
		return d == bb.y || (bb.y+bb.h) == d2;
	}
	private boolean testZ(AABB bb){
		double d = Math.min(Math.max(this.z, bb.z),this.z+this.l);
		double d2 = Math.min(Math.max(this.z, bb.z+bb.l),this.z+this.l);
		return d == bb.z || (bb.z+bb.l) == d2;
	}
	/**
	 * Compares to AxisAlignedBoundingBoxes and outputs the collision values in a double array
	 *
	 * @param bb the AxisAlignedBoundingBox that is being compared against this one
	 * @return a double array containing collision values in the format {MaxX,MaxY,MaxZ,MinX,MinY,MinZ}
	 */
	public double[] compareAABB(AABB bb){
		double maxx = Math.min(Math.max(this.x, bb.x),this.x+this.w);
		double minx = Math.min(Math.max(this.x, bb.x+bb.w),this.x+this.w);
		double maxy = Math.min(Math.max(this.y, bb.y),this.y+this.h);
		double miny = Math.min(Math.max(this.y, bb.y+bb.h),this.y+this.h);
		double maxz = Math.min(Math.max(this.z, bb.z),this.z+this.l);
		double minz = Math.min(Math.max(this.z, bb.z+bb.l),this.z+this.l);
		return new double[] {maxx,maxy,maxz,minx,miny,minz};
	}

	public AABB translateAABB(double dx, double dy, double dz){
		this.x += dx;
		this.y += dy;
		this.z += dz;
		return this;
	}

	public AABB setPosition(double x, double y, double z){
		switch(align){
		case ACTUAL_COORDS:
			this.x = x;
			this.y = y;
			this.z = z;
			break;
		case BOTTOM_CENTER:
			this.x = x-(w/2d);
			this.y = y;
			this.z = z-(l/2d);
			break;
		case ABSOLUTE_CENTER:
			this.x = x-(w/2d);
			this.y = y-(h/2d);
			this.z = z-(l/2d);
			break;
		}
		return this;
	}

	public void renderDebugBox(){
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex3d(x, y, z);
		GL11.glVertex3d(x+w, y, z);
		GL11.glVertex3d(x+w, y, z+l);
		GL11.glVertex3d(x, y, z+l);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex3d(x, y+h, z);
		GL11.glVertex3d(x+w, y+h, z);
		GL11.glVertex3d(x+w, y+h, z+l);
		GL11.glVertex3d(x, y+h, z+l);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(x, y, z);
		GL11.glVertex3d(x, y+h, z);
		GL11.glVertex3d(x+w, y, z);
		GL11.glVertex3d(x+w, y+h, z);
		GL11.glVertex3d(x+w, y, z+l);
		GL11.glVertex3d(x+w, y+h, z+l);
		GL11.glVertex3d(x, y, z+l);
		GL11.glVertex3d(x, y+h, z+l);
		GL11.glEnd();
	}
}
