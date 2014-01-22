package aggroforce.event.listener;

import aggroforce.event.MouseEvent;

public interface IMouseListener extends IEventListener {

	String name = "mouse";

	void onMouseEvent(MouseEvent evt);
}
