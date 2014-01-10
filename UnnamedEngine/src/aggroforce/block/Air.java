package aggroforce.block;

public class Air extends Block {

	public Air() {
		super(0);
	}

	@Override
	public boolean isSolidOpaque(){
		return false;
	}

	@Override
	public int renderType(){
		return -1;
	}

}
