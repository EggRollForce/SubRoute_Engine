package aggroforce.player;

import aggroforce.entity.Entity;

public class Player extends Entity{

	public Player(){
		super(0,500,0);
	}

	@Override
	public boolean isAffectedByGravity(){
		return true;
	}
}
