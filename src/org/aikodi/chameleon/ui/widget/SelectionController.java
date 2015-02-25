package org.aikodi.chameleon.ui.widget;


public interface SelectionController<W> {

	public void setContext(Object element);
	
	public void show();
	
	public void hide();
	
	public W widget();
}
