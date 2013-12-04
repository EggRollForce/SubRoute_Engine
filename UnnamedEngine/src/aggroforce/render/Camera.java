package aggroforce.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import aggroforce.game.Game;

public class Camera{
	public static float x,y,z;
	public static float pitch,yaw;
	public static Vector3f veiwVec = new Vector3f();
	public static Camera instance;
	private static float mouseSens = 0.1f;
	private static float sprintMod = 4f;
	private static float defMovSpd = 0.005f;
	private static float movSpd = 0.005f;

	public Camera(){
		instance = this;
		x = 0;
		y = 0;
		z = 0;
		pitch = 0;
		yaw = 0;
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
		float temp = pitch - dy*mouseSens;
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
		GL11.glRotatef(pitch, 1, 0, 0);
		GL11.glRotatef(yaw, 0, 1, 0);
		GL11.glTranslatef(x, y, z);
	}
}
