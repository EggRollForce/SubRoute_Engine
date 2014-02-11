package aggroforce.player;

import aggroforce.entity.Entity;
import aggroforce.phys.util.AABB;

public class Player extends Entity{


	public Player(){
		super(0,500,0);
		this.headOffset = new double[] {0.5,1.5,0.5};
		this.boundingBox = new AABB(0,0,0,0.6,1.8,0.6);
	}

	@Override
	public boolean isAffectedByGravity(){
		return true;
	}

	@Override
	public AABB getBoundingBox(){
		return new AABB(0,0,0,0.6,1.8,0.6).setPosition(xPos+0.2, yPos, zPos+0.2);
	}


}
