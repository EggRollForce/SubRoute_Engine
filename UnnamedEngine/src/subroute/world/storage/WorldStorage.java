package subroute.world.storage;

import subroute.render.RenderEngine;
import subroute.render.Renderer;
import subroute.world.WorldLoader;
import subroute.world.sector.Sector;


public class WorldStorage implements IWorldAccess{

	private int MAX_SEGMENTS_RADIUS = 10;
	private SegLoader segLoad = new SegLoader();
	private static WorldStorage instance;
	private static WorldLoader loader;

	public WorldStorage(WorldLoader wl){
		if(instance == null){
			loader = wl;
			instance = this;
			this.generateSquare(wl, this.MAX_SEGMENTS_RADIUS, 0, 0);
		}
	}
	private void generateSquare(WorldLoader wl, int radius, int sx, int sy){
		this.generateSquare(wl, radius, sx, sy, 1);
	}
	private void generateSquare(WorldLoader wl, int radius, int startx, int starty, int level){
		System.out.println("Attempting square gen");
		if(level<=radius){
			int x = startx;
			int y = starty;
			for(int i = 0; i < (level*2)-1; i++){
				System.out.println("Generating segment: "+x+","+y);
				segLoad.addSegment(wl.generateSegment(x,y).setWorld(this));
				x--;
			}
			for(int i = 0; i < (level*2)-1; i++){
				System.out.println("Generating segment: "+x+","+y);
				segLoad.addSegment(wl.generateSegment(x,y).setWorld(this));
				y--;
			}
			for(int i = 0; i < (level*2)-1; i++){
				System.out.println("Generating segment: "+x+","+y);
				segLoad.addSegment(wl.generateSegment(x,y).setWorld(this));
				x++;
			}
			for(int i = 0; i < (level*2)-1; i++){
				System.out.println("Generating segment: "+x+","+y);
				segLoad.addSegment(wl.generateSegment(x,y).setWorld(this));
				y++;
			}
			level++;
			startx++;
			starty++;
			this.generateSquare(wl, radius, startx, starty, level);
		}else{
			return;
		}
	}

	int advx,advy,sx=0,sy=0,level=1,rot=0;
	double inc = 0;
	private boolean done = false;
	public void loadNextRenderer(){
		if(!done){
			if(level>this.MAX_SEGMENTS_RADIUS){
				done= true;
				return;
			}
			switch(rot){
				case 0:
					renderInSegment(sx,sy);
					sx--;
					if(sx<-(level-1)){
						rot++;
					}
					break;
				case 1:
					renderInSegment(sx,sy);
					sy--;
					if(sy<-(level-1)){
						rot++;
					}
					break;
				case 2:
					renderInSegment(sx,sy);
					sx++;
					if(sx>=level-1){
						rot++;
					}
					break;
				case 3:
					renderInSegment(sx,sy);
					sy++;
					if(sy>=level-1){
						rot=0;
						level++;
						sx = level-1;
						sy = level-1;
					}
					break;
			}
		}
	}
	private void renderInSegment(int x, int y){
		Sector seg = segLoad.getSegmentAt(x, y);
		if(seg != null&&seg.getRenderer()==null){
			Renderer render = new Renderer(seg);
			render.updateRenderable();
			RenderEngine.renderBlocks.addRenderer(render);
			render.markForUpdate(true);
//			System.out.println("Sucessfully loaded segment at x:"+(lastx)+" y:"+(lasty));
		}
	}

	@Override
	public int getBlockIdAt(int x, int y, int z) {
		int sx = (int)Math.floor((x)/16d);
		int sy = (int)Math.floor((z)/16d);
		Sector seg = segLoad.getSegmentAt(sx, sy);
		if(seg!=null){
			return seg.getBlockIdAt(x, y, z);
		}
		return 0;
	}

	public static WorldStorage getInstance(){
		return instance;
	}


	@Override
	public boolean setBlockAt(int x, int y, int z, int id) {
		int sx = (int)Math.floor((x)/16d);
		int sy = (int)Math.floor((z)/16d);
		Sector seg = segLoad.getSegmentAt(sx, sy);
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
	int lastx,lasty;
	public void checkGenRadius(){
		if(!check){
		check = false;
		if(inc >= this.MAX_SEGMENTS_RADIUS*360){
			inc = 0;
			check = true;
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
			Sector seg = null;
			segLoad.addSegment(seg = WorldStorage.loader.generateSegment(x+cx, y+cy).setWorld(this));
			Renderer render = new Renderer(seg);
			seg.renderBlocks(render);
			RenderEngine.renderBlocks.addRenderer(render);
			render.markForUpdate(true);
		}
		}
	}

	@Override
	public boolean blockExistsAt(int x, int y, int z) {
		return this.getBlockIdAt(x, y, z)!=0;
	}

	public void updateAdjSegments(int x, int y){
		Sector seg;
		if((seg=segLoad.getSegmentAt(x+1, y))!=null){
			if(seg.getRenderer()!=null){
				seg.getRenderer().markForUpdate(true);
			}
		}
		if((seg=segLoad.getSegmentAt(x, y+1))!=null){
			if(seg.getRenderer()!=null){
				seg.getRenderer().markForUpdate(true);
			}
		}
		if((seg=segLoad.getSegmentAt(x-1, y))!=null){
			if(seg.getRenderer()!=null){
				seg.getRenderer().markForUpdate(true);
			}
		}
		if((seg=segLoad.getSegmentAt(x, y-1))!=null){
			if(seg.getRenderer()!=null){
				seg.getRenderer().markForUpdate(true);
			}
		}
	}

	public int[] getNextBlockInColumn(boolean down, int x, int z, int height){
		int y = height;
		int id = -1;
		do{
			if(!(y<=0||y>=1024)){
				id = this.getBlockIdAt(x, y, z);
				if(down){
					y--;
				}else{
					y++;
				}
			}else{
				id = -1;
			}
		}while(id==0&&id!=-1);
		return new int[] {id,y};
	}
}
