package be.kuleuven.cs.distrinet.chameleon.ui.widget;



public interface WidgetFactory<W> {

	public SelectionController createCheckbox(String text, boolean initialState, CheckboxListener listener);
	
	public SelectionController createTristateTree(TreeContentProvider contentProvider);
}
