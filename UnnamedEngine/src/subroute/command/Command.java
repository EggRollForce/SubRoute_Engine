package subroute.command;

public abstract class Command {

	abstract public String name();

	abstract public String usage();

	abstract public void handleCommand(String[] args);
}
