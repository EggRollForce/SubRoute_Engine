package subroute.phys.util;

import org.lwjgl.opengl.GL11;

public class AABB {

	public enum Alignment{
		ACTUAL_COORDS,
		BOTTOM_CENTER,
		ABSOLUTE_CENTER
	}
	private Alignment align;
	public double minX,minY,minZ,maxX,maxY,maxZ;
	private double width,length,height;

	//Create a new AxisAlignedBoundingBox
	public AABB(Alignment a, double x, double y, double z, double width, double height, double length){
		this.align = a;
		this.width = width;
		this.length = length;
		this.height = height;
		switch(align){
		case ACTUAL_COORDS:
			this.minX = x;
			this.minY = y;
			this.minZ = z;
			this.maxX = x+width;
			this.maxY = y+height;
			this.maxZ = z+length;
			break;
		case BOTTOM_CENTER:
			this.minX = x-(width/2d);
			this.minY = y;
			this.minZ = z-(length/2d);
			this.maxX = x+(width/2d);
			this.maxY = y+height;
			this.maxZ = z+(length/2d);
			break;
		case ABSOLUTE_CENTER:
			this.minX = x-(width/2d);
			this.minY = y-(height/2d);
			this.minZ = z-(length/2d);
			this.maxX = x+(width/2d);
			this.maxY = y+(height/2d);
			this.maxZ = z+(length/2d);
			break;
		}
	}

	public AABB(Alignment a, double width, double height, double length){
		this(a,0,0,0,width,height,length);
	}
	public double getWidth(){
		return width;
	}
	public double getHeight(){
		return height;
	}
	public double getLength(){
		return length;
	}
	public Alignment getAlignment(){
		return align;
	}

	public double calcXIntersect(AABB comp, double x){
		if(comp.maxY>this.minY&&comp.minY<this.maxY){
			if(comp.maxZ>this.minZ&&comp.minZ<this.maxZ){
				double temp;
				if(x>0&&comp.maxX<=this.minX){
					temp = this.minX-comp.maxX;
					if(temp<x){
						x=temp;
					}
				}
				if(x<0&&comp.minX>=this.maxX){
					temp = this.maxX-comp.minX;
					if(temp>x){
						x=temp;
					}
				}
				return x;
			}else{
				return x;
			}
		}else{
			return x;
		}
	}
	public double calcZIntersect(AABB comp, double z){
		if(comp.maxX>this.minX&&comp.minX<this.maxX){
			if(comp.maxY>this.minY&&comp.minY<this.maxY){
				double temp;
				if(z>0&&comp.maxZ<=this.minZ){
					temp = this.minZ-comp.maxZ;
					if(temp<z){
						z=temp;
					}
				}
				if(z<0&&comp.minZ>=this.maxZ){
					temp = this.maxZ-comp.minZ;
					if(temp>z){
						z=temp;
					}
				}
				return z;
			}else{
				return z;
			}
		}else{
			return z;
		}
	}
	public double calcYIntersect(AABB comp, double y){
		if(comp.maxX>this.minX&&comp.minX<this.maxX){
			if(comp.maxZ>this.minZ&&comp.minZ<this.maxZ){
				double temp;
				if(y>0&&comp.maxY<=this.minY){
					temp = this.minY-comp.maxY;
					if(temp<y){
						y=temp;
					}
				}
				if(y<0&&comp.minY>=this.maxY){
					temp = this.maxY-comp.minY;
					if(temp>y){
						y=temp;
					}
				}
				return y;
			}else{
				return y;
			}
		}else{
			return y;
		}
	}

	public void copyExpanded(AABB a, double x, double y, double z){
		double xmx = this.maxX;
		double xmn = this.minX;
		double ymx = this.maxY;
		double ymn = this.minY;
		double zmx = this.maxZ;
		double zmn = this.minZ;

		if(x<0){
			xmn += x;
		}
		if(x>0){
			xmx += x;
		}
		if(y<0){
			ymn += y;
		}
		if(y>0){
			ymx += y;
		}
		if(z<0){
			zmn += z;
		}
		if(z>0){
			zmx += z;
		}
		AABB.setBounds(a, xmn, ymn, zmn, xmx, ymx, zmx);
	}
	public static void setBounds(AABB bb, double x1, double y1, double z1, double x2, double y2, double z2){
		bb.minX = x1;
		bb.minY = y1;
		bb.minZ = z1;
		bb.maxX = x2;
		bb.maxY = y2;
		bb.maxZ = z2;
	}

	public static boolean intersects(AABB bb1, AABB bb2){
		return bb1.testX(bb2) || bb1.testY(bb2) || bb1.testZ(bb2);
	}

	private boolean testX(AABB bb){
		double d = Math.min(Math.max(this.minX, bb.minX),this.minX+this.maxX);
		double d2 = Math.min(Math.max(this.minX, bb.minX+bb.maxX),this.minX+this.maxX);
		return d == bb.minX || (bb.minX+bb.maxX) == d2;
	}
	private boolean testY(AABB bb){
		double d = Math.min(Math.max(this.minY, bb.minY),this.minY+this.maxY);
		double d2 = Math.min(Math.max(this.minY, bb.minY+bb.maxY),this.minY+this.maxY);
		return d == bb.minY || (bb.minY+bb.maxY) == d2;
	}
	private boolean testZ(AABB bb){
		double d = Math.min(Math.max(this.minZ, bb.minZ),this.minZ+this.maxZ);
		double d2 = Math.min(Math.max(this.minZ, bb.minZ+bb.maxZ),this.minZ+this.maxZ);
		return d == bb.minZ || (bb.minZ+bb.maxZ) == d2;
	}
	/**
	 * Compares to AxisAlignedBoundingBoxes and outputs the collision values in a double array
	 *
	 * @param bb the AxisAlignedBoundingBox that is being compared against this one
	 * @return a double array containing collision values in the format {MaxX,MaxY,MaxZ,MinX,MinY,MinZ}
	 */
	public double[] compareAABB(AABB bb){
		double maxx = Math.min(Math.max(this.minX, bb.minX),this.minX+this.maxX);
		double minx = Math.min(Math.max(this.minX, bb.minX+bb.maxX),this.minX+this.maxX);
		double maxy = Math.min(Math.max(this.minY, bb.minY),this.minY+this.maxY);
		double miny = Math.min(Math.max(this.minY, bb.minY+bb.maxY),this.minY+this.maxY);
		double maxz = Math.min(Math.max(this.minZ, bb.minZ),this.minZ+this.maxZ);
		double minz = Math.min(Math.max(this.minZ, bb.minZ+bb.maxZ),this.minZ+this.maxZ);
		return new double[] {maxx,maxy,maxz,minx,miny,minz};
	}

	public AABB translate(double dx, double dy, double dz){
		this.minX += dx;
		this.minY += dy;
		this.minZ += dz;
		this.maxX += dx;
		this.maxY += dy;
		this.maxZ += dz;
		return this;
	}

	public AABB setPosition(double x, double y, double z){
		return this.setPosition(align, x, y, z);
	}
	public AABB setPosition(Alignment a, double x, double y, double z){
		switch(a){
		case ACTUAL_COORDS:
			this.minX = x;
			this.minY = y;
			this.minZ = z;
			this.maxX = x+width;
			this.maxY = y+height;
			this.maxZ = z+length;
			break;
		case BOTTOM_CENTER:
			this.minX = x-(width/2d);
			this.minY = y;
			this.minZ = z-(length/2d);
			this.maxX = x+(width/2d);
			this.maxY = y+height;
			this.maxZ = z+(length/2d);
			break;
		case ABSOLUTE_CENTER:
			this.minX = x-(width/2d);
			this.minY = y-(height/2d);
			this.minZ = z-(length/2d);
			this.maxX = x+(width/2d);
			this.maxY = y+(height/2d);
			this.maxZ = z+(length/2d);
			break;
		}
		return this;
	}

	public void renderDebugBox(){
		this.renderDebugBox(1, 1, 1, 1);
	}
	public void renderDebugBox(float r, float g, float b, float a){
		GL11.glColor4f(r, g, b, a);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex3d(minX, minY, minZ);
		GL11.glVertex3d(maxX, minY, minZ);
		GL11.glVertex3d(maxX, minY, maxZ);
		GL11.glVertex3d(minX, minY, maxZ);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex3d(minX, maxY, minZ);
		GL11.glVertex3d(maxX, maxY, minZ);
		GL11.glVertex3d(maxX, maxY, maxZ);
		GL11.glVertex3d(minX, maxY, maxZ);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(minX, minY, minZ);
		GL11.glVertex3d(minX, maxY, minZ);
		GL11.glVertex3d(maxX, minY, minZ);
		GL11.glVertex3d(maxX, maxY, minZ);
		GL11.glVertex3d(maxX, minY, maxZ);
		GL11.glVertex3d(maxX, maxY, maxZ);
		GL11.glVertex3d(minX, minY, maxZ);
		GL11.glVertex3d(minX, maxY, maxZ);
		GL11.glEnd();
	}
}
