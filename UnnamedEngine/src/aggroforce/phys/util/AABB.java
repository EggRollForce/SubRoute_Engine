package aggroforce.phys.util;

import org.lwjgl.opengl.GL11;

public class AABB {

	private double x,y,z,w,l,h;

	//Create a new AxisAlignedBoundingBox
	public AABB(double x, double y, double z, double width, double height, double length){
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

	public AABB translateAABB(double dx, double dy, double dz){
		this.x += dx;
		this.y += dy;
		this.z += dz;
		return this;
	}

	public AABB setPosition(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
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
