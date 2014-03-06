package subroute.entity;

import subroute.Game;
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
	public double xPos,yPos,zPos;
	public double lastX,lastY,lastZ;
	protected double[] headOffset = new double[] {0,0,0};
	public float xVel = 0f, yVel = 0f, zVel = 0f;
	protected float pitch = 0f, yaw = 0f;
	public double grav = 9.806;

	public Entity(double posx, double posy, double posz){
		EventRegistry.EVENT_BUS.registerListener(this);
		this.xPos = posx;
		this.yPos = posy;
		this.zPos = posz;
	}

	//Sets the velocity used for position calculation
	public void setVelocity(float xVel, float yVel, float zVel){
		if(!this.stasis){
			this.xVel = xVel;
			this.yVel = yVel;
			this.zVel = zVel;
		}
	}

	//Adds velocity to the current velocity
	public void addVelocity(float xVel, float yVel, float zVel){
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

		isCollidingWalls();

		if(isCollidingGround()){
			if(Math.signum(yVel)!=1){
				if(nbdat!=null){
					yVel = 0;
					yPos = nbdat[1]+1;
				}else{
					yVel = 0;
				}
			}
		}
		this.updateVelocity();
		if(this.isAffectedByGravity()){
			yVel -= (grav)*this.getDeltaSec();
		}
	}

	private double getDeltaSec(){
		return (Game.getDelta()/1000d);
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
//total perspective
	public Side[] getSides(boolean totalPerspective){
		WorldStorage wld = WorldStorage.getInstance();

		if(totalPerspective == true){
			return Side.getValidSides();
		}
		else{
			return Side.getFaces();
		}

	}

	//a response for collision detection and boolean statement. 2 methods
	//retun was originally Side[]
	public void isCollidingWalls(){

		if(WorldStorage.getInstance()!=null){
		WorldStorage wld = WorldStorage.getInstance();


		//[0]N [1]S [2]E [3]W





		}

	}


	private int[] nbdat;
	public boolean isCollidingGround(){
		//if collision detected return true. default return false
		if(WorldStorage.getInstance()!=null){
			nbdat = WorldStorage.getInstance().getNextBlockInColumn(true, (int)Math.floor(xPos), (int)Math.floor(zPos), (int)Math.floor(yPos+1));
			if(nbdat[0]!=-1){
				return yPos<=nbdat[1]+1;
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
