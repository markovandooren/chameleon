package org.aikodi.chameleon.ui.widget;

import org.aikodi.chameleon.ui.widget.checkbox.CheckboxListener;

public abstract class CheckboxSelector implements Selector {

	public CheckboxSelector(String message, boolean initialValue) {
		if(message == null) {
			throw new IllegalArgumentException();
		}
		_message = message;
		_selection = initialValue;
	}
	
	@Override
	public <W> SelectionController<? extends W> createControl(WidgetFactory<W> factory) {
		return factory.createCheckbox(_message, _selection, new CheckboxListener(){
			@Override
			public void selectionChanged(boolean selection) {
				_selection = selection;
			}
		});
	}
	
	// We store the selection state because this object is longer living than the UI widget,
	// which is disposed when another configuration is selected.
	private boolean _selection;

	public boolean selected() {
		return _selection;
	}
	
	private String _message;

	/**
	 * Does nothing by default because a checkbox selector is 
	 * typically not context-sensitive.
	 */
	@Override
	public void setContext(Object context) {
	}

}
