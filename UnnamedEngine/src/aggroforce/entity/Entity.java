package aggroforce.entity;

import aggroforce.block.Block;
import aggroforce.event.EventHandler;
import aggroforce.event.EventRegistry;
import aggroforce.event.listener.IEventListener;
import aggroforce.event.tick.EntityTick;
import aggroforce.game.Game;
import aggroforce.phys.util.AABB;
import aggroforce.util.Side;
import aggroforce.world.storage.WorldStorage;

public class Entity implements IEventListener{

	boolean stasis = false;
	protected AABB boundingBox;
	public double xPos,yPos,zPos;
	public double lastX,lastY,lastZ;
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
		this.lastX = xPos;
		this.lastY = yPos;
		this.lastZ = zPos;
		if(!stasis){
			Side[] walls;
			if((walls = this.isCollidingWalls())!=null){
				for(Side side : walls){
					if(side == null){
						continue;
					}else{
						if(side == Side.SOUTH){
							if((xVel*(Game.getDelta()/1000d))+(xPos+(this.boundingBox.getWidth()/2d)) < Math.ceil(xPos)+1){
								if(Math.signum(xVel)==-1){
									xVel = 0;
									this.xPos = Math.ceil(xPos)-1+(this.boundingBox.getWidth()/2d);
								}
							}
						}else if(side == Side.NORTH){
							if(xPos+(xVel*(Game.getDelta()/1000d)) > Math.floor(xPos)-1){
								if(Math.signum(xVel)==1){
									xVel = 0;
									this.xPos = Math.floor(xPos)+1-(this.boundingBox.getWidth()/2d);
								}
							}
						}else if(side == Side.WEST){
							if((zVel*(Game.getDelta()/1000d))+(zPos+(this.boundingBox.getLength()/2d)) < Math.ceil(zPos)+1){
								if(Math.signum(zVel)==-1){
									zVel = 0;
									this.zPos = Math.ceil(zPos)-1+(this.boundingBox.getLength()/2d);
								}
							}
						}else if(side == Side.EAST){
							if(zPos+(zVel*(Game.getDelta()/1000d)) > Math.floor(zPos)-1){
								if(Math.signum(zVel)==1){
									zVel = 0;
									this.zPos = Math.floor(zPos)+1-(this.boundingBox.getLength()/2d);
								}
							}
						}
					}
				}
			}
			if(this.isCollidingGround()){
				System.out.println("true");
				if(Math.signum(yVel)!=1){
					this.yVel = 0;
				}
				this.yPos = Math.ceil(this.yPos);
				this.updateVelocity();
				if(nbdat!=null){
					xVel *= Block.blocks[nbdat[0]].getSlipperyness();
	////				yVel *= Block.blocks[blockid].getSlipperyness();
					zVel *= Block.blocks[nbdat[0]].getSlipperyness();
				}
			}else{
				this.updateVelocity();
			}
			if(this.isAffectedByGravity()){
				this.yVel -= (grav*0.5)*(Math.pow((Game.getDelta()/100d),2));
			}
		}
	}

	private void updateVelocity(){
		this.xPos += xVel*(Game.getDelta()/100d);
		if((yPos + yVel*(Game.getDelta()/100f))<=0){
			this.yPos = 0;
			this.yVel = 0;
		}else{
			this.yPos += yVel*(Game.getDelta()/100d);
//			System.out.println(yVel);

		}
		this.zPos += zVel*(Game.getDelta()/100d);
//		System.out.println(zVel);
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
	public Side[] isCollidingWalls(){
		if(WorldStorage.getInstance()!=null){
		WorldStorage wld = WorldStorage.getInstance();
		Side[] arr = new Side[4];
		int index = 0;
		boolean isNull = true;
		for(Side side : Side.getFaces()){
			int xpos = (int)Math.floor(xPos)+side.getX();
			int zpos = (int)Math.floor(zPos)+side.getZ();
			int id;
			if((id = wld.getBlockIdAt(xpos,(int)Math.floor(yPos+1), zpos))!=0){
				arr[index] = side;
				isNull = false;
			}else if((id = wld.getBlockIdAt(xpos,(int)Math.floor(yPos+2), zpos))!=0){
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
				return yPos<=nbdat[1]+1;
			}
		}else if(nbdat[0]>=1){
			System.out.println("true");
			return true;
		}

		System.out.println("false");
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
