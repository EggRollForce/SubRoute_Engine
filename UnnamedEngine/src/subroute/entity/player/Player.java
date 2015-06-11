package subroute.entity.player;

import subroute.entity.Entity;
import subroute.phys.util.AABB;
import subroute.phys.util.AABB.Alignment;

public class Player extends Entity{

	private boolean flying = false;

	public Player(){
		super(0,500,0);
		this.headOffset = new double[] {0,1.5,0};
		this.boundingBox = new AABB(Alignment.BOTTOM_CENTER,0.6,1.8,0.6);
		this.collideBox = new AABB(Alignment.BOTTOM_CENTER,0.6,1.8,0.6);
	}

	@Override
	public boolean isAffectedByGravity(){
		return !flying;
	}

	@Override
	public AABB getBoundingBox(){
		return this.boundingBox.setPosition(xPos, yPos, zPos);
	}

	public void setIsFlying(boolean flying){
		this.flying = flying;
	}

	public boolean isFlying(){
		return flying;
	}

}
