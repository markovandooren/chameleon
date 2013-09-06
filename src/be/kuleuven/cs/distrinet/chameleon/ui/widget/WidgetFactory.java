package be.kuleuven.cs.distrinet.chameleon.ui.widget;

import be.kuleuven.cs.distrinet.chameleon.ui.widget.checkbox.CheckboxListener;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.list.ComboBoxController;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.list.ComboBoxListener;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.list.ListContentProvider;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.tree.CheckStateProvider;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.tree.TreeContentProvider;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.tree.TreeListener;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.tree.TristateTreeController;


/**
 * A factory interface for creating UI widgets. Each factory method
 * return a controller that allows setting the input for the control.
 * 
 * @author Marko van Dooren
 *
 * @param <W> The type of the widgets.
 */
public interface WidgetFactory<W> {

	/**
	 * Create a checkbox widget.
	 * 
	 * @param text The label of the checkbox.
	 * @param initialState The initial state of the checkbox.
	 * @param listener A listener that is notified when the state of the checkbox changes.
	 */
	public SelectionController<? extends W> createCheckbox(String text, boolean initialState, CheckboxListener listener);
	
	public <V> TristateTreeController<? extends W> createTristateTree(
			TreeContentProvider<V> contentProvider, 
			LabelProvider provider, 
			TreeListener<V> listener,
			CheckStateProvider<V> checkStateProvider);
	
	public <V> ComboBoxController<? extends W> createComboBox(ListContentProvider<V> contentProvider, LabelProvider provider, ComboBoxListener listener, int baseOneDefaultSelection);
}
