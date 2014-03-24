package subroute;




import java.io.File;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import subroute.audio.AudioEngine;
import subroute.block.Block;
import subroute.console.DebugConsole;
import subroute.event.EventRegistry;
import subroute.event.tick.EntityTick;
import subroute.input.Input;
import subroute.render.RenderEngine;


public class Game {

	private static final String version = "0.0.0.01";

	private static Game instance;
	private static int scrW = 800;
	private static int scrH = 600;
	private static DisplayMode display;
	private long lastFPS = getTime();
	private long lastFrame;
	private static int fps;
	private int cfps;
	private static int delta;
	private static long startTime;

	public static void main(String[] args){
		try{
			boolean debug = false;
			for(int i = 0; i<args.length; i++){
				String a = args[i].trim();
				switch(a){
					case "-console_enable":
						debug=true;
						continue;
					case "-width":
					case "-scrw":
						int w;
						if(i+1<=args.length){
							w=Integer.decode(args[i+1]);
							if(w==0){
								System.err.println("Width is not valid!");
							}else{
								System.out.println("Setting screen width to "+w);
								Game.scrW = w;
							}
						}
						continue;
					case "-height":
					case "-scrh":
						int h;
						if(i+1<=args.length){
							h=Integer.decode(args[i+1]);
							if(h==0){
								System.err.println("Height is not valid!");
							}else{
								System.out.println("Setting screen height to "+h);
								Game.scrH = h;
							}
						}
						continue;
				}
				if(a.indexOf("-console_enable")!=-1){
					debug=true;
					break;
				}
			}
			if(debug){
				new DebugConsole();
				System.out.println("Debug console initialized");
			}else{
				System.out.println("Starting game normally...");
			}
			System.out.print("Start Args: ");
			for(String a: args){
				System.out.print(a+" ");
			}
			System.out.println();
			System.out.println("Linking native libraries.");
			System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath()+File.separator+Game.getOSName());
			System.out.println("Starting SubRoute ver:"+version);
			new Game(args);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static Game instance(){
		return instance;
	}

	public Game(String[] args){
		Game.instance = this;
		Game.startTime = getTime();
		try {
			display = new DisplayMode(scrW,scrH);
			Display.setDisplayMode(display);
			Display.setTitle("SubRoute [DEV BUILD] Ver:"+version);
			Display.setResizable(true);
			Display.create();
//			Display.setVSyncEnabled(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		Block.setupBlocks();
		new AudioEngine();
		new RenderEngine();
		new Input();

		this.initOpenGL();
		updateDelta();
		//Main game loop that is exited when the display is closed
		while (!Display.isCloseRequested()) {
			//Update the time between frames
			updateDelta();
			//Recreate the veiwport if the window was resized
			if(Display.wasResized()){
				GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
			}
			//Check all input events for mouse and keyboard (Controller coming soon!)
			Input.checkEvents();
			//Update the entities by passing this event to the bus
			EventRegistry.EVENT_BUS.postEvent(new EntityTick());
			//Only render if the display is visible
			if(Display.isVisible()){
				RenderEngine.instance.renderLoop();
			}
			//Update the FPS counter
			updateFPS();
			AudioEngine.instance().loop();
			Display.update();
		}
		AL.destroy();
		Display.destroy();
		if(DebugConsole.instance()!=null){
			DebugConsole.instance().dispose();
		}
	}
	public long getTime(){
		return (Sys.getTime()*1000)/Sys.getTimerResolution();
	}

	public int getTimeMinutes(){
		return (int) Math.floor(getTimeSeconds()/60);
	}
	public double getTimeSeconds(){
		return getTime()/1000d;
	}
	public int getTimeHours(){
		return (int) Math.floor(getTimeMinutes()/60);
	}
	public String getTimeAsString(){
		return this.getTimeHours()+":"+this.getTimeMinutes()%60+":"+this.getTimeSeconds()%60;
	}
	public void updateFPS(){
		if(getTime()-lastFPS > 1000){
			fps = cfps;
			cfps=0;
			lastFPS+=1000;
		}
		cfps++;
	}

	public static String getTimeSinceStart(){
		if(Game.instance!=null){
			long time = Game.instance.getTime()-Game.startTime;
			double sec = time/1000d;
			int min = (int) Math.floor(sec/60);
			int hr = (int) Math.floor(min/60);
			return String.format("%02d",hr)+":"+String.format("%02d",min%60)+":"+String.format("%02d",(int)(sec%60));//hr,min%60,sec%60);//hr+":"+min%60+":"+(int)Math.floor(sec%60);
		}else{
			return "initializing";
		}
	}
	public static String getOSName(){
		String name = System.getProperty("os.name","generic").toLowerCase();
		if(name.indexOf("win") >= 0){
			return "windows";
		}else if(name.indexOf("mac") >= 0){
			return "macosx";
		}else if(name.indexOf("lin") >= 0){
			return "linux";
		}
		return "unknown";
	}

	private void updateDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		Game.delta = delta;
	}

	//Returns delta in millisecs
	public static int getDelta(){
		return delta;
	}

	public static int getFps(){
		return fps;
	}

	public void initOpenGL(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f,((float)Game.instance().getScreenWidth()/(float)Game.instance().getScreenHeight()),0.1f,10000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glClearDepth(1.0);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public int getScreenWidth(){
		return Display.getWidth();
	}
	public int getScreenHeight(){
		return Display.getHeight();
	}

}
