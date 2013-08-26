package be.kuleuven.cs.distrinet.chameleon.ui.widget;

import be.kuleuven.cs.distrinet.chameleon.ui.widget.checkbox.CheckboxListener;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.list.ComboBoxController;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.list.ComboBoxListener;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.list.ListContentProvider;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.tree.TreeContentProvider;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.tree.TreeListener;



public interface WidgetFactory<W> {

	public SelectionController<? extends W> createCheckbox(String text, boolean initialState, CheckboxListener listener);
	
	public <V> SelectionController<? extends W> createTristateTree(TreeContentProvider<V> contentProvider, LabelProvider provider, TreeListener<V> listener);
	
	public <V> ComboBoxController<? extends W> createComboBox(ListContentProvider<V> contentProvider, LabelProvider provider, ComboBoxListener listener, int baseOneDefaultSelection);
}
