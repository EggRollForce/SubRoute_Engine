package subroute.command;

public class TestCommand extends Command{

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "test";
	}

	@Override
	public String usage() {
		// TODO Auto-generated method stub
		return "A command used for testing";
	}

	@Override
	public void handleCommand(String[] args) {
	}

}
