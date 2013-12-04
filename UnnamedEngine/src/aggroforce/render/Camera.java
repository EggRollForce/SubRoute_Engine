package aggroforce.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import aggroforce.game.Game;

public class Camera{
	public static double x=0,y=0,z=0;
	public static double pitch,yaw;
	public static Vector3f veiwVec = new Vector3f();
	public static Camera instance;
	private static float mouseSens = 0.1f;
	private static float sprintMod = 10f;
	private static float defMovSpd = 0.005f;
	private static float movSpd = 0.005f;

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
		if(!(temp > -85)){
			return;
		}else if(!(temp < 85)){
			return;
		}else if(temp > -85 && temp < 85){
			pitch = temp;
		}
		yaw += dx*mouseSens;
	}
	public boolean isBoundingBoxInFutstrum(double x, double y, double z, double width, double height){
		return false;
	}
	public static void updateVeiwVector(){
		Camera.veiwVec.set((float)Math.cos(Math.toRadians(pitch)),0f,(float)Math.sin(Math.toRadians(yaw)));
	}
	public static void camTransform(){
		updateVeiwVector();
		GL11.glLoadIdentity();
		GL11.glRotated(pitch, 1, 0, 0);
		GL11.glRotated(yaw, 0, 1, 0);
		GL11.glTranslated(x, y, z);
	}
	public static void camUnTransform(){
		GL11.glRotated(-pitch, 1, 0, 0);
		GL11.glRotated(-yaw, 0, 1, 0);
		GL11.glTranslated(-x, -y, -z);
	}
	public static void camTranslateOnly(){
		GL11.glTranslated(x, y, z);
	}
}
