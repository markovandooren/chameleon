package be.kuleuven.cs.distrinet.chameleon.ui.widget;



public interface WidgetFactory<W> {

	public SelectionController<? extends W> createCheckbox(String text, boolean initialState, CheckboxListener listener);
	
	public <V> SelectionController<? extends W> createTristateTree(TreeContentProvider<V> contentProvider, LabelProvider provider, TreeListener<V> listener);
}
