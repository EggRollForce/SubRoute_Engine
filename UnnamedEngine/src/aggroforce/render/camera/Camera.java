package aggroforce.render.camera;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import aggroforce.game.Game;
import aggroforce.world.storage.WorldStorage;

public class Camera{
	public static double x=0,y=0,z=0;
	public static double pitch,yaw;
	public static Vector3f veiwVec = new Vector3f();
	public static Camera instance;
	private static float mouseSens = 0.1f;
	private static float sprintMod = 10f;
	private static float defMovSpd = 0.005f;
	private static float movSpd = 0.005f;
	private static boolean thirdPerson = false;

	public Camera(){
		instance = this;
		pitch = 0;
		yaw = 0;
	}
	public Camera(double x, double y, double z){
		this();
		Camera.x = x;
		Camera.y = y;
		Camera.z = z;
	}

	public static void forward(){
		x -= movSpd * (float)Math.sin(Math.toRadians(yaw)) * Game.instance().getDelta();
		z += movSpd * (float)Math.cos(Math.toRadians(yaw)) * Game.instance().getDelta();
	}
	public static void backward(){
		x += movSpd * (float)Math.sin(Math.toRadians(yaw)) * Game.instance().getDelta();
		z -= movSpd * (float)Math.cos(Math.toRadians(yaw)) * Game.instance().getDelta();
	}
	public static void strafeRight(){
		x -= movSpd * (float)Math.sin(Math.toRadians(yaw+90)) * Game.instance().getDelta();
		z += movSpd * (float)Math.cos(Math.toRadians(yaw+90)) * Game.instance().getDelta();
	}
	public static void strafeLeft(){
		x -= movSpd * (float)Math.sin(Math.toRadians(yaw-90)) * Game.instance().getDelta();
		z += movSpd * (float)Math.cos(Math.toRadians(yaw-90)) * Game.instance().getDelta();
	}
	public static void up(){
		y -= movSpd * Game.instance().getDelta();
	}
	public static void down(){
		y += movSpd * Game.instance().getDelta();
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
	}
	public boolean isBoundingBoxInFutstrum(double x, double y, double z, double width, double height){
		return false;
	}
	public static void updateVeiwVector(){
		Camera.veiwVec.set((float)Math.sin(Math.toRadians(yaw))*(float)Math.cos(Math.toRadians(pitch)),-(float)Math.sin(Math.toRadians(pitch)),(float)Math.cos(Math.toRadians(yaw))*(float)Math.cos(Math.toRadians(pitch)));
	}
	private static double x2,y2,z2;
	private static int radius = 10;
	public static void camTransform(){
		updateVeiwVector();
		GL11.glLoadIdentity();
		GL11.glRotated(pitch, 1, 0, 0);
		GL11.glRotated(yaw, 0, 1, 0);
		if(thirdPerson){
			GL11.glTranslated(x2, y2, z2);
		}else{
			GL11.glTranslated(x, y, z);
		}
	}
	public static void thirdPersonOn(){
		thirdPerson = true;
		x2 = x-Math.sin(Math.toRadians(yaw+180))*Math.cos(Math.toRadians(pitch))*radius;
		y2 = y-Math.sin(Math.toRadians(pitch))*radius;
		z2 = z-Math.cos(Math.toRadians(yaw))*Math.cos(Math.toRadians(pitch))*radius;
	}
	public static void thirdPersonOff(){
		thirdPerson =false;
	}
	public static void camUnTransform(){
		GL11.glRotated(-pitch, 1, 0, 0);
		GL11.glRotated(-yaw, 0, 1, 0);
		GL11.glTranslated(-x, -y, -z);
	}
	public static void camTranslateOnly(){
		GL11.glTranslated(x, y, z);
	}
	private int r = 10;
	public BlockTarget getLookTargetBlock(){
		int x = 0,y = 0,z = 0,id = 0;
		float mul = 0;
		boolean first = true;
		do{
			mul+=0.001f;
			double temp = Camera.z+(veiwVec.z*mul);
			int x2 = -(int)Math.floor(Camera.x-(veiwVec.x*mul)+1);
			int y2 =(int)((veiwVec.y*mul)-Camera.y)+1;
			int z2 = -(int)(temp+(Math.signum(temp)!=1?0:1));
			if(x2!=x||y2!=y||z2!=z||first){
				first = false;
				x=x2;y=y2;z=z2;
				id = WorldStorage.getInstance().getBlockIdAt(x, y,z);
			}
		}while(id==0&&!(mul>=r));
		if(!(mul>=r)){
			return new BlockTarget(x, y-1, z,id);
		}
		return null;
	}
}
