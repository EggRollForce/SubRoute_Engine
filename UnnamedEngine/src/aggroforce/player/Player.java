package aggroforce.player;

import aggroforce.entity.Entity;
import aggroforce.phys.util.AABB;
import aggroforce.phys.util.AABB.Alignment;

public class Player extends Entity{


	public Player(){
		super(0,500,0);
		this.headOffset = new double[] {0,1.5,0};
		this.boundingBox = new AABB(Alignment.BOTTOM_CENTER,0.6,1.8,0.6);
	}

	@Override
	public boolean isAffectedByGravity(){
		return true;
	}

	@Override
	public AABB getBoundingBox(){
		return this.boundingBox.setPosition(xPos, yPos, zPos);
	}


}
