package subroute.render;

public interface IRenderable {

	public boolean isMarkedForUpdate();

	public void renderTo(Renderer r);
}
