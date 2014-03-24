package subroute.command;

import subroute.console.ConsoleCommandRegistry;

public class ListCommand extends Command {

	@Override
	public String name() {
		return "list";
	}

	@Override
	public String usage() {
		return "lists availible commands";
	}

	@Override
	public void handleCommand(String[] args) {
		ConsoleCommandRegistry.printAllCommands(true);
	}

}