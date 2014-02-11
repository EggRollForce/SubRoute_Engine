package aggroforce.entity;

import aggroforce.block.Block;
import aggroforce.event.EventHandler;
import aggroforce.event.EventRegistry;
import aggroforce.event.listener.IEventListener;
import aggroforce.event.tick.EntityTick;
import aggroforce.game.Game;
import aggroforce.phys.util.AABB;
import aggroforce.world.storage.WorldStorage;

public class Entity implements IEventListener{

	boolean stasis = false;
	protected AABB boundingBox;
	protected double xPos,yPos,zPos;
	protected double[] headOffset = new double[] {0,0,0};
	protected float xVel = 0f, yVel = 0f, zVel = 0f;
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
		if(!stasis){
			if(this.isAffectedByGravity()){
				this.yVel -= ((grav*2d)*(Game.getDelta()/1000d));
			}
			if(this.isColliding()){
				if(Math.signum(yVel)!=1){
					this.yVel = 0;
				}
				this.updateVelocity();
				xVel *= Block.blocks[blockid].getSlipperyness();
//				yVel *= Block.blocks[blockid].getSlipperyness();
				zVel *= Block.blocks[blockid].getSlipperyness();
			}else{
				this.updateVelocity();
			}
		}
	}

	private void updateVelocity(){
		this.xPos += xVel*(Game.getDelta()/1000d);
		this.yPos += yVel*(Game.getDelta()/1000d);
		this.zPos += zVel*(Game.getDelta()/1000d);
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


	//a response for collision detection and boolean statement. 2 methods

	private int blockid;
	public boolean isColliding(){
		//if collision detected return true. default return false
		AABB bb = new AABB(0,0,0,1,1,1);
		if(WorldStorage.getInstance()!=null){
			if((blockid = WorldStorage.getInstance().getBlockIdAt((int)Math.floor(xPos+0.5), (int)Math.floor(yPos+1), (int)Math.floor(zPos+0.5)))!=0){
				return AABB.intersects(boundingBox.setPosition(xPos-Math.floor(xPos), yPos-Math.floor(yPos), zPos-Math.floor(zPos)), bb);
			}
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
