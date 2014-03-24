package subroute.console;

import java.util.ArrayList;
import java.util.Arrays;

import subroute.command.Command;
import subroute.command.ListCommand;
import subroute.command.TestCommand;

public class ConsoleCommandRegistry {

	public static final ConsoleCommandRegistry COMMAND_BUS =  new ConsoleCommandRegistry();
	private static ArrayList<Command> cmds = new ArrayList<Command>();

	protected static final void initCommands(){
		COMMAND_BUS.addCommand(new ListCommand());
		COMMAND_BUS.addCommand(new TestCommand());
	}
	public boolean addCommand(Command cmd){
		if(!cmds.isEmpty()){
			for(Command c : cmds){
				if(c.name()==cmd.name()){
					return false;
				}
			}
		}
		cmds.add(cmd);
		System.out.println("Added the "+cmd.name()+" command");
		return true;
	}

	protected static void executeCommand(String cmdln){
		String[] s = cmdln.trim().split(" ");
		for(Command c : cmds){
			if(c.name().trim().equals(s[0].trim())){
				fireCommand(c,Arrays.copyOfRange(s, 1, s.length));
			}
		}
	}
	private static void fireCommand(Command cmd, String[] args){
		if(cmd!=null){
			cmd.handleCommand(args);
		}
	}
	public static void printAllCommands(boolean desc){
		for(Command c : cmds){
			System.out.println(c.name()+(desc?": "+c.usage():""));
		}
	}
}
