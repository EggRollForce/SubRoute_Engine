package subroute.entity;

import subroute.Game;
import subroute.block.Block;
import subroute.event.EventHandler;
import subroute.event.EventRegistry;
import subroute.event.listener.IEventListener;
import subroute.event.tick.EntityTick;
import subroute.phys.util.AABB;
import subroute.util.Side;
import subroute.world.storage.WorldStorage;

public class Entity implements IEventListener{

	boolean stasis = false;
	protected AABB boundingBox;
	protected AABB collideBox;
	public double xPos,yPos,zPos;
	public double lastX,lastY,lastZ;
	protected double[] headOffset = new double[] {0,0,0};
	public float xVel = 0f, yVel = 0f, zVel = 0f;
	protected float pitch = 0f, yaw = 0f;
	public double grav = 9.806;
	public boolean onGround, isCollided, isCollidedVertical, isCollidedHorizontal;

	public Entity(double posx, double posy, double posz){
		EventRegistry.EVENT_BUS.registerListener(this);
		this.xPos = posx;
		this.yPos = posy;
		this.zPos = posz;
	}

	//Sets the velocity used for position calculation
	public void setVelocity(float xVel, float yVel, float zVel){
		this.onGround = false;
		if(!this.stasis){
			this.xVel = xVel;
			this.yVel = yVel;
			this.zVel = zVel;
		}
	}

	//Adds velocity to the current velocity
	public void addVelocity(float xVel, float yVel, float zVel){
		this.onGround = false;
		if(!this.stasis){
			this.xVel += xVel;
			this.yVel += yVel;
			this.zVel += zVel;
		}
	}

	public double[] getHeadOffset(){
		return headOffset;
	}

	public void setPosition(double x, double y, double z){
		this.xPos = x;
		this.yPos = y;
		this.zPos = z;
	}

	public void setAngles(float pitch, float yaw){
		this.pitch = pitch;
		this.yaw = yaw;
	}
	//Update function called by the event bus
	@EventHandler
	public final void onUpdate(EntityTick e){
		this.lastX = xPos;
		this.lastY = yPos;
		this.lastZ = zPos;
		if(this.isAffectedByGravity()){
			yVel -= (grav)*this.getDeltaSec();
		}
		if(this.onGround){
			xVel *= 0.1;
			zVel *= 0.1;
		}
		this.moveEntity(this.xVel, this.yVel, this.zVel);
//		this.updateVelocity();
	}

	private void updateCollisions(){
		if(bxmax&&(xPos+xVel*this.getDeltaSec())>=xmax-(this.boundingBox.getWidth()/2d)){
			xVel = 0;
			xPos = xmax-(this.boundingBox.getWidth()/2d);
		}
		if(bxmin&&(xPos+xVel*this.getDeltaSec())<=xmin+(this.boundingBox.getWidth()/2d)){
			xVel = 0;
			xPos = xmin+(this.boundingBox.getWidth()/2d);
		}
		if(bzmax&&(zPos+zVel*this.getDeltaSec())>=zmax-(this.boundingBox.getLength()/2d)){
			zVel = 0;
			zPos = zmax-(this.boundingBox.getLength()/2d);
		}
		if(bzmin&&(zPos+zVel*this.getDeltaSec())<=zmin+(this.boundingBox.getLength()/2d)){
			zVel = 0;
			zPos = zmin+(this.boundingBox.getLength()/2d);
		}
		if((yPos+yVel*this.getDeltaSec())>=ymax-this.boundingBox.getHeight()){
			yVel = 0;
			yPos = ymax-this.boundingBox.getHeight();
		}
		if((yPos+yVel*this.getDeltaSec())<=ymin){
			yVel = 0;
			yPos = ymin;
		}
	}
	public double getDeltaSec(){
		int dint = Game.getDelta()==0?1:Game.getDelta();
		return (dint/1000D);
	}
	private void updateVelocity(){
		this.xPos += xVel*this.getDeltaSec();
		if(!((yPos + yVel)>=0)){
			this.yPos = 0;
			this.yVel = 0;
		}else{
			this.yPos += yVel*this.getDeltaSec();
		}
		this.zPos += zVel*this.getDeltaSec();
	}

	public double getXPos(){return this.xPos;}
	public double getYPos(){return this.yPos;}
	public double getZPos(){return this.zPos;}
	public float getXVel(){return this.xVel;}
	public float getYVel(){return this.yVel;}
	public float getZVel(){return this.zVel;}
	public float getPitch(){return this.pitch;}
	public float getYaw(){return this.yaw;}

	//find out if the entity is affected by gravitational pull of the world
	public boolean isAffectedByGravity(){
		return true;
	}

	public void setBoudingBox(AABB bb){
		this.boundingBox = bb;
	}
	public AABB getBoundingBox(){
		return this.boundingBox;
	}
	double ymax, ymin, xmax, xmin, zmax, zmin;
	boolean bxmax, bxmin, bzmax, bzmin;
	public void moveEntity(double x, double y, double z){
		x*=this.getDeltaSec();
		y*=this.getDeltaSec();
		z*=this.getDeltaSec();
		this.boundingBox.setPosition(xPos, yPos, zPos);
		this.boundingBox.copyExpanded(collideBox, x, y, z);
		int imin = (int)Math.floor(collideBox.minX), imax = (int)Math.floor(collideBox.maxX+1);
		int kmin = (int)Math.floor(collideBox.minZ), kmax = (int)Math.floor(collideBox.maxZ+1);
		int jmin = (int)Math.floor(collideBox.minY), jmax = (int)Math.floor(collideBox.maxY+1);
		double px = x;
		double py = y;
		double pz = z;

		WorldStorage wld = WorldStorage.getInstance();
		if(wld!=null){
		for(int i = imin; i < imax; i++){
			for(int k = kmin; k < kmax; k++){
				for(int j = jmin-1; j < jmax; j++){
					if(wld.blockExistsAt(i, j, k)){
						int id = wld.getBlockIdAt(i, j, k);
						AABB bbb = Block.blocks[id].getBoundingBox(wld, i, j, k);
						y = bbb.calcYIntersect(boundingBox, y);
					}
				}
			}
		}
		boundingBox.translate(0, y, 0);
		for(int i = imin; i < imax; i++){
			for(int k = kmin; k < kmax; k++){
				for(int j = jmin-1; j < jmax; j++){
					if(wld.blockExistsAt(i, j, k)){
						int id = wld.getBlockIdAt(i, j, k);
						AABB bbb = Block.blocks[id].getBoundingBox(wld, i, j, k);
						x = bbb.calcXIntersect(boundingBox, x);
					}
				}
			}
		}
		boundingBox.translate(x, 0, 0);
		for(int i = imin; i < imax; i++){
			for(int k = kmin; k < kmax; k++){
				for(int j = jmin-1; j < jmax; j++){
					if(wld.blockExistsAt(i, j, k)){
						int id = wld.getBlockIdAt(i, j, k);
						AABB bbb = Block.blocks[id].getBoundingBox(wld, i, j, k);
						z = bbb.calcZIntersect(boundingBox, z);
					}
				}
			}
		}
		boundingBox.translate(0, 0, z);
//		for(int i = imin; i <= imax; i++){
//			for(int k = kmin; k <= kmax; k++){
//				for(int j = jmin; j <= jmax; j++){
//					if(wld.blockExistsAt(i, j, k)){
//						int id = wld.getBlockIdAt(i, j, k);
//						AABB bbb = Block.blocks[id].getBoundingBox(wld, i, j, k);
//						if(j>jmin&&j<jmax&&(bbb.maxX>ymin)&&(bbb.minY<ymax)){
//							if(k>kmin&&k<kmax){
//								if(i<=imin+1){
//									bbb.renderDebugBox(1f,0f,0f,0.5f);
//									m = bbb.maxX;
//									if(!bxmin){
//										xmin = m;
//									}
//									bxmin = true;
//									if(m>xmin){
//										xmin = m;
//									}
//								}
//								if(i>=imax-1){
//									bbb.renderDebugBox(0.5f,0f,0f,0.5f);
//									m = bbb.minX;
//									if(!bxmax){
//										xmax = m;
//									}
//									bxmax = true;
//									if(m<xmax){
//										xmax=m;
//									}
//								}
//							}
//							if(i>imin&&i<imax){
//								if(k<=kmin+1){
//									bbb.renderDebugBox(0f,0f,1f,0.5f);
//									m = bbb.maxZ;
//									if(!bzmin){
//										zmin = m;
//									}
//									bzmin = true;
//									if(m>zmin){
//										zmin = m;
//									}
//								}
//								if(k>=kmax-1){
//									bbb.renderDebugBox(0f,0f,0.5f,0.5f);
//									m = bbb.minZ;
//									if(!bzmax){
//										zmax = m;
//									}
//									bzmax = true;
//									if(m<zmax){
//										zmax = m;
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		//X Plane Debug
//		GL11.glColor4f(1f, 0f, 0f, 0.5f);
//		GL11.glDisable(GL11.GL_CULL_FACE);
//		GL11.glBegin(GL11.GL_QUADS);
//		GL11.glVertex3f((float)(boundingBox.minX+x), jmin, kmin);
//		GL11.glVertex3f((float)(boundingBox.minX+x), jmax, kmin);
//		GL11.glVertex3f((float)(boundingBox.minX+x), jmax, kmax);
//		GL11.glVertex3f((float)(boundingBox.minX+x), jmin, kmax);
//		GL11.glEnd();
//		//Y Plane Debug
//		GL11.glEnable(GL11.GL_CULL_FACE);
//		GL11.glColor4f(0f, 1f, 0f, 0.5f);
//		GL11.glDisable(GL11.GL_CULL_FACE);
//		GL11.glBegin(GL11.GL_QUADS);
//		GL11.glVertex3f(imin, (float)(boundingBox.minY+y), kmin);
//		GL11.glVertex3f(imax, (float)(boundingBox.minY+y), kmin);
//		GL11.glVertex3f(imax, (float)(boundingBox.minY+y), kmax);
//		GL11.glVertex3f(imin, (float)(boundingBox.minY+y), kmax);
//		GL11.glEnd();
//		GL11.glEnable(GL11.GL_CULL_FACE);
//		//Z Plane Debug
//		GL11.glColor4f(0f, 0f, 1f, 0.5f);
//		GL11.glDisable(GL11.GL_CULL_FACE);
//		GL11.glBegin(GL11.GL_QUADS);
//		GL11.glVertex3f(imin, jmin, (float)(boundingBox.minZ+z));
//		GL11.glVertex3f(imin, jmax, (float)(boundingBox.minZ+z));
//		GL11.glVertex3f(imax, jmax, (float)(boundingBox.minZ+z));
//		GL11.glVertex3f(imax, jmin, (float)(boundingBox.minZ+z));
//		GL11.glEnd();
//		GL11.glEnable(GL11.GL_CULL_FACE);
		//Collide Box Debug
		collideBox.renderDebugBox();

		this.isCollidedVertical = py != y;
		this.isCollidedHorizontal = px != x || px != y;
		this.isCollided = this.isCollidedHorizontal || this.isCollidedVertical;
		this.onGround = py!=y && py < 0;

		xVel = (float) (x / this.getDeltaSec());
		yVel = (float) (y / this.getDeltaSec());
		zVel = (float) (z / this.getDeltaSec());
		xPos = (boundingBox.maxX+boundingBox.minX)/2D;
		yPos = (boundingBox.minY);
		zPos = (boundingBox.maxZ+boundingBox.minZ)/2D;
		}

	}


	//a response for collision detection and boolean statement. 2 methods
	public Side[] isCollidingWalls(){
		if(WorldStorage.getInstance()!=null){
		WorldStorage wld = WorldStorage.getInstance();
		Side[] arr = new Side[4];
		int index = 0;
		boolean isNull = true;
		for(Side side : Side.getFaces()){
			int xpos = (int)Math.floor(xPos)+side.getX();
			int zpos = (int)Math.floor(zPos)+side.getZ();
			if((wld.getBlockIdAt(xpos,(int)Math.floor(yPos+1), zpos))!=0){
				arr[index] = side;
				isNull = false;
			}else if((wld.getBlockIdAt(xpos,(int)Math.floor(yPos+2), zpos))!=0){
				arr[index] = side;
				isNull = false;
			}
			index++;
		}
			if(!isNull){
				return arr;
			}else{
				return null;
			}
		}else{
			return Side.getFaces();
		}
	}

	private int[] nbdat;
	public boolean isCollidingGround(){
		//if collision detected return true. default return false
		if(WorldStorage.getInstance()!=null){
			nbdat = WorldStorage.getInstance().getNextBlockInColumn(true, (int)Math.floor(xPos), (int)Math.floor(zPos), (int)Math.floor(yPos+1));
			if(nbdat[0]!=-1){
				return yPos<=nbdat[1];
			}
		}else{
			return true;
		}
		return false;
	}

	//is entity nonmoving or moving
	public void setStasisTrue(boolean val){
		stasis = val;
	}

	@Deprecated
	public void setPathOfTravel() throws Exception{
		if(stasis == true){
			throw new Exception("staticObject...CANNOT set path of travel");
		}
		else{
			//set coordinates for travel
		}
	}

}
