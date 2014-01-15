package aggroforce.event.listener;

import aggroforce.event.MouseEvent;

public interface IMouseListener extends IEventListener {
	public final String type = "";
	public void onEvent(MouseEvent evt);
}
