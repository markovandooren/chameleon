package be.kuleuven.cs.distrinet.chameleon.ui.widget;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;

public interface SelectionController {

	public void setContext(Element element);
	
	public void show();
	
	public void hide();
}
