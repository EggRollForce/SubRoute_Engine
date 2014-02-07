package aggroforce.entity;

public class Entity {

	boolean stasis = true;

	public Entity(double posx, double posy, double posz){

	}

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

	public void setPathOfTravel() throws Exception{
		if(stasis == true){
			throw new Exception("staticObject...CANNOT set path of travel");
		}
		else{
			//set coordinates for travel
		}
	}

}
