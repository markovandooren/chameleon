package be.kuleuven.cs.distrinet.chameleon.ui.widget;



public interface WidgetFactory<W> {

	public Input createCheckbox(String text, boolean initialState, CheckboxListener listener);
	
	public Input createTristateTree(TreeContentProvider contentProvider);
}
