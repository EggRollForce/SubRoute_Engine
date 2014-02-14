package subroute.render.camera;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import subroute.Game;
import subroute.entity.Entity;
import subroute.util.Side;
import subroute.world.storage.WorldStorage;


public class Camera{
	public static double x=0,y=0,z=0;
	public static double pitch,yaw;
	public static Vector3f veiwVec = new Vector3f();
	public static Camera instance;
	private static float mouseSens = 0.1f;
	private static float sprintMod = 2f;
	private static float defMovSpd = 1f;
	private static float movSpd = 1f;
	private static boolean thirdPerson = false;
	private static Entity boundEnt;
	private static boolean isBound = false;

	public Camera(){
		instance = this;
		pitch = 0;
		yaw = 0;
	}
	public Camera(Entity ent){
		instance = this;
		boundEnt = ent;
		isBound = true;
	}
	public Camera(double x, double y, double z){
		this();
		Camera.x = x;
		Camera.y = y;
		Camera.z = z;
	}

	public static void forward(){
		if(isBound){
			x = 0;
			z = 0;
		}
		x += movSpd * (float)Math.sin(Math.toRadians(yaw));
		z -= movSpd * (float)Math.cos(Math.toRadians(yaw));
		if(isBound){
			boundEnt.setVelocity((float)x, boundEnt.getYVel(), (float)z);
		}
	}
	public static void backward(){
		if(isBound){
			x = 0;
			z = 0;
		}
		x -= movSpd * (float)Math.sin(Math.toRadians(yaw));
		z += movSpd * (float)Math.cos(Math.toRadians(yaw));
		if(isBound){
			boundEnt.setVelocity((float)x, boundEnt.getYVel(), (float)z);
		}
	}
	public static void strafeRight(){
		if(isBound){
			x = 0;
			z = 0;
		}
		x += movSpd * (float)Math.sin(Math.toRadians(yaw+90));
		z -= movSpd * (float)Math.cos(Math.toRadians(yaw+90));
		if(isBound){
			boundEnt.setVelocity((float)x, boundEnt.getYVel(), (float)z);
		}
	}
	public static void strafeLeft(){
		if(isBound){
			x = 0;
			z = 0;
		}
		x += movSpd * (float)Math.sin(Math.toRadians(yaw-90));
		z -= movSpd * (float)Math.cos(Math.toRadians(yaw-90));
		if(isBound){
			boundEnt.setVelocity((float)x, boundEnt.getYVel(), (float)z);
		}
	}
	public static void up(){
		y += movSpd * Game.getDelta();
		if(isBound){
			boundEnt.setVelocity(boundEnt.getXVel(), 2, boundEnt.getZVel());
		}
	}
	public static void down(){
		y -= movSpd * Game.getDelta();
		if(isBound){
//			boundEnt.setVelocity(boundEnt.getXVel(), -movSpd, boundEnt.getZVel());
		}
	}
	public static void sprintOn(){
		movSpd = defMovSpd*sprintMod;
	}
	public static void sprintOff(){
		movSpd = defMovSpd;
	}
	public static void mouseIn(float dx, float dy){
		double temp = pitch - dy*mouseSens;
		yaw += dx*mouseSens;
		if(!(temp > -90)){
			return;
		}else if(!(temp < 90)){
			return;
		}else if(temp > -90 && temp < 90){
			pitch = temp;
		}
		if(isBound){
			boundEnt.setAngles((float)pitch, (float)yaw);
		}
	}
	public boolean isBoundingBoxInFutstrum(double x, double y, double z, double width, double height){
		return false;
	}

	public static float[] getVeiwAT(){
		return new float[] {Camera.veiwVec.x,Camera.veiwVec.y,Camera.veiwVec.z};
	}
	public static float[] getViewUP(){
		return new float[] {(float)Math.sin(Math.toRadians(yaw))*(float)Math.cos(Math.toRadians(pitch+90)),-(float)Math.sin(Math.toRadians(pitch+90)),(float)Math.cos(Math.toRadians(yaw))*(float)Math.cos(Math.toRadians(pitch+90))};
	}
	public static void updateVeiwVector(){
		Camera.veiwVec.set((float)Math.sin(Math.toRadians(yaw))*(float)Math.cos(Math.toRadians(pitch)),-(float)Math.sin(Math.toRadians(pitch)),(float)Math.cos(Math.toRadians(yaw))*(float)Math.cos(Math.toRadians(pitch)));
	}


	private static double radius = 10;
	public static void camTransform(){
		updateVeiwVector();
		GL11.glLoadIdentity();
		if(thirdPerson){
			GL11.glTranslated(0, 0, -radius);
		}
		if(!isBound){
			GL11.glRotated(pitch, 1, 0, 0);
			GL11.glRotated(yaw, 0, 1, 0);
			GL11.glTranslated(x, y, z);
		}else{
			double[] off = boundEnt.getHeadOffset();
			GL11.glRotated(boundEnt.getPitch(), 1, 0, 0);
			GL11.glRotated(boundEnt.getYaw(), 0, 1, 0);
			GL11.glTranslated(-boundEnt.getXPos()-off[0], -boundEnt.getYPos()-off[1], -boundEnt.getZPos()-off[2]);
		}
	}
	public static void setThirdPerson(boolean thrdper){
		thirdPerson = thrdper;
	}
	public static boolean isThirdPerson(){
		return thirdPerson;
	}
	public static void camUnTransform(){
		GL11.glRotated(-pitch, 1, 0, 0);
		GL11.glRotated(-yaw, 0, 1, 0);
		GL11.glTranslated(-x, -y, -z);
	}
	public static void camTranslateOnly(){
		GL11.glTranslated(x, y, z);
	}
	private int r = 100;
	public BlockTarget getLookTargetBlock(){
		int x = 0,y = 0,z = 0,id = 0;
		double[] prevpos = new double[3];
		double ax,ay,az;

		if(isBound){
			double[] off = boundEnt.getHeadOffset();
			ax = boundEnt.getXPos()+off[0];
			ay = boundEnt.getYPos()+off[1];
			az = boundEnt.getZPos()+off[2];
		}else{
			ax = Camera.x;
			ay = Camera.y;
			az = Camera.z;
		}


		float mul = 0;
		boolean first = true;
		do{
			double[] pos = new double[3];
			mul+=0.001f;
			double temp = az-(veiwVec.z*mul);
			pos[0] = (ax+(veiwVec.x*mul));
			pos[1] =((veiwVec.y*mul)+ay)+1;
			pos[2] = (temp-(Math.signum(temp)!=-1?0:1));
			if((int)Math.floor(pos[0])!=x||(int)pos[1]!=y||(int)pos[2]!=z||first){
				first = false;
				x=(int)Math.floor(pos[0]);y=(int)pos[1];z=(int)pos[2];
				id = WorldStorage.getInstance().getBlockIdAt(x,y,z);
				if(id!=0){
					break;
				}
			}
			prevpos = pos;

		}while(!(mul>=r));
		if(!(mul>=r)){
			return new BlockTarget(x, y, z, id, this.getSideForHit(x, y, z, prevpos));
		}
		return null;
	}

	public Side getSideForHit(int x, int y, int z, double[] pos){
		pos[0] -= x;
		pos[1] -= y;
		pos[2] -= z;

		if(pos[0]<0){
			return Side.SOUTH;
		}else if(pos[0]>=1){
			return Side.NORTH;
		}else if(pos[1]<=0){
			return Side.DOWN;
		}else if(pos[1]>=1){
			return Side.UP;
		}else if(pos[2]<=0){
			return Side.WEST;
		}else if(pos[2]>=0){
			return Side.EAST;
		}
		return Side.NONE;
	}
	public static Entity getBoundEntity(){
		return boundEnt;
	}
}
