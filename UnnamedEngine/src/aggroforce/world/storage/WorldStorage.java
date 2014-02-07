package aggroforce.world.storage;

import java.util.ArrayList;

import aggroforce.render.Renderer;
import aggroforce.world.WorldLoader;
import aggroforce.world.segment.Segment;

public class WorldStorage implements IWorldAccess{

	private static ArrayList<Renderer> rnders = new ArrayList<Renderer>();
	private static ArrayList<Segment> queue = new ArrayList<Segment>();
	private int MAX_SEGMENTS_RADIUS = 10;
	private SegLoader segLoad = new SegLoader();
	private boolean needsUpdate = false;
	private boolean loaded = false;
	private static WorldStorage instance;
	private static WorldLoader loader;
	public static Renderer rendererpass2;

	public WorldStorage(WorldLoader wl){
		if(instance == null){
			rendererpass2 = new Renderer();
			loader = wl;
			instance = this;
			int lastx=0,lasty=0;
			for(double i = 0; i <= this.MAX_SEGMENTS_RADIUS*360; i+=0.5){
					int x = (int)(Math.sin(Math.toRadians(i))*(int)(i/360));
					int y = (int)(Math.cos(Math.toRadians(i))*(int)(i/360));
					if(x==lastx&&y==lasty){
						continue;
					}else{
						lastx = x;
						lasty = y;
					}
					segLoad.addSegment(wl.generateSegment(x,y).setWorld(this));
			}
		}
	}

	int nextx,nexty,lastx=0,lasty=0;

	public boolean isLoaded(){
		return loaded;
	}
	double inc = 0;
	private boolean done = false;
	public void loadNextRenderer(){
		if(!done){
		this.updateNeeded();
		while(true){
			int x = (int)(Math.cos(Math.toRadians(inc))*(int)(inc/360d));
			int y = (int)(Math.sin(Math.toRadians(inc))*(int)(inc/360d));
			inc+=0.5;
			if(x==lastx&&y==lasty){
				continue;
			}else{
				lastx = x;
				lasty = y;
				break;
			}
		}
		if(inc > this.MAX_SEGMENTS_RADIUS*360){
			inc = 0;
			this.done = true;
			this.updated();
			return;
		}
		Segment seg = segLoad.getSegmentAt(lastx, lasty);
		if(seg != null&&seg.getRenderer()==null){
			Renderer render = new Renderer();
			seg.renderBlocks(render);
			rnders.add(render);
			render.setUpdated(true);
			System.out.println("Sucessfully loaded segment at x:"+(lastx)+" y:"+(lasty));
		}
		}
	}

	@Override
	public int getBlockIdAt(int x, int y, int z) {
		int sx = (int)Math.floor((x)/16d);
		int sy = (int)Math.floor((z)/16d);
		Segment seg = segLoad.getSegmentAt(sx, sy);
		if(seg!=null){
			return seg.getBlockIdAt(x, y, z);
		}
		return 0;
	}

	public static ArrayList<Renderer> getRenderers(){
		if(instance.segLoad.checkForUpdate()){
			instance.updated();
			System.out.println("All renderers updated");
		}
		if(rnders.remove(rendererpass2)||!rnders.contains(rendererpass2)){
			rnders.add(rendererpass2);
		}
		return rnders;
	}

	public static ArrayList<Renderer> getUpdateRenderers(){
		return null;
	}

	public static WorldStorage getInstance(){
		return instance;
	}


	@Override
	public boolean setBlockAt(int x, int y, int z, int id) {
		this.updateNeeded();
		int sx = (int)Math.floor((x)/16d);
		int sy = (int)Math.floor((z)/16d);
		Segment seg = segLoad.getSegmentAt(sx, sy);
		if(seg!=null){
			boolean success = seg.setBlockAt(x, y, z, id);
			if(success){
				this.updateAdjSegments(sx, sy);
			}
			return success;
		}
		return false;
	}
	public static boolean check = true;
	private int cx,cy;
	public void needCheck(int x, int y){
		cx = x;
		cy = y;
		check = false;
	}
	public void checkGenRadius(){
		if(!check){
		check = false;
		this.updateNeeded();
		if(inc >= this.MAX_SEGMENTS_RADIUS*360){
			inc = 0;
			check = true;
			this.updated();
			return;
		}
		int x = -1,y = -1;
		while(true){
			x = (int)(Math.sin(Math.toRadians(inc))*(int)(inc/360));
			y = (int)(Math.cos(Math.toRadians(inc))*(int)(inc/360));
			inc+=0.5;
			if(x==lastx&&y==lasty){
				continue;
			}else{
				lastx = x;
				lasty = y;
				break;
			}
		}
		if(segLoad.getSegmentAt(x+cx, y+cy)==null){
			Segment seg;
			segLoad.addSegment(seg = WorldStorage.loader.generateSegment(x+cx, y+cy).setWorld(this));
			Renderer render = new Renderer();
			seg.renderBlocks(render);
			rnders.add(render);
			render.setUpdated(true);
			this.updateAdjSegments(cx+x, cy+y);
		}
		}
	}

	public void addToRenderUpdateQueue(Segment seg){
		if(!queue.contains(seg)){
			queue.add(seg);
		}
	}

	boolean queueDone = false;
	public void updateRenderQueue(){
		if(check){
			if(queue.size()>0){
				queueDone = false;
				Segment seg = queue.get(0);
				queue.remove(seg);
				seg.renderUpdate();
			}else if(!queueDone){
				queueDone = true;
				this.updateNeeded();
			}
		}
	}

	public void updateAdjSegments(int x, int y){
			Segment seg;
			if((seg=segLoad.getSegmentAt(x+1, y))!=null){
				this.addToRenderUpdateQueue(seg);
			}
			if((seg=segLoad.getSegmentAt(x, y+1))!=null){
				this.addToRenderUpdateQueue(seg);
			}
			if((seg=segLoad.getSegmentAt(x-1, y))!=null){
				this.addToRenderUpdateQueue(seg);
			}
			if((seg=segLoad.getSegmentAt(x, y-1))!=null){
				this.addToRenderUpdateQueue(seg);
			}
	}

	@Override
	public void updateNeeded() {
		this.needsUpdate = true;
	}

	@Override
	public boolean getIsUpdateNeeded() {
		return this.needsUpdate;
	}

	private void updated(){
		this.needsUpdate = false;
	}
}
