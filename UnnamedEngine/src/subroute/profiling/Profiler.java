package subroute.profiling;

import java.util.HashMap;
import java.util.Set;

public class Profiler {

	private static HashMap<String,Section> sections = new HashMap<String,Section>();
	private static Section runningSection = null;
	private static Profiler instance;

	public Profiler(){
		if(this.instance==null){
			instance = this;
		}
	}

	public static void startSection(String name){
		if(runningSection==null){
			if(sections.get(name)==null){
				runningSection = instance.new Section(name);
				sections.put(name,runningSection);
			}else{
				runningSection = sections.get(name);
			}

			runningSection.start();
		}else{
			System.err.println("Cannot start new section with one already running!");
		}
	}

	public static void stopSection(){
		if(runningSection!=null){
			runningSection.stop();
			runningSection = null;
		}else{
			System.err.println("Cannot stop section which was never started!");
		}
	}

	public static void stopStartSection(String name){
		Profiler.stopSection();
		Profiler.startSection(name);
	}

	public static Section getSection(String name){
		return sections.get(name);
	}

	public static Set<String> getSectionNames(){
		return sections.keySet();
	}

	public class Section{

		public String name;
		public long start;
		public long end;
		public long elapsed;
		public boolean running;

		public Section(String name){
			this.name = name;
		}

		public void start(){
			running = true;
			start = System.nanoTime();
		}

		public void stop(){
			running = false;
			end = System.nanoTime();
			elapsed = end-start;
		}
	}
}
