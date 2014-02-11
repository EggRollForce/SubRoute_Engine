package aggroforce.entity;

import aggroforce.event.EventHandler;
import aggroforce.event.EventRegistry;
import aggroforce.event.listener.IEventListener;
import aggroforce.event.tick.EntityTick;
import aggroforce.game.Game;

public class Entity implements IEventListener{

	boolean stasis = false;
	protected double xPos,yPos,zPos;
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
	public void onUpdate(EntityTick e){
//		System.out.println("Entity Updated");
		if(!stasis){
			if(this.isAffectedByGravity()){
				this.yVel -= (grav*(Game.getDelta()/1000d));
			}
			this.xPos += xVel*(Game.getDelta()/1000d);
			this.yPos += yVel*(Game.getDelta()/1000d);
			this.zPos += zVel*(Game.getDelta()/1000d);
		}
	}

	public double getXPos(){return this.xPos;}
	public double getYPos(){return this.yPos;}
	public double getZPos(){return this.zPos;}
	public float getPitch(){return this.pitch;}
	public float getYaw(){return this.yaw;}



	//find out if the entity is affected by gravitational pull of the world
	public boolean isAffectedByGravity(){
		return true;
	}


	//a response for collision detection and boolean statement. 2 methods

	public boolean collisionDetected(){
		//if collision detected return true. default return false
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
