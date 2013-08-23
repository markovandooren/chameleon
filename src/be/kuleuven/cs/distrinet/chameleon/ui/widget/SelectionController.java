package be.kuleuven.cs.distrinet.chameleon.ui.widget;


public interface SelectionController<W> {

	public void setContext(Object element);
	
	public void show();
	
	public void hide();
	
	public W widget();
}
