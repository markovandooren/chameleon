package be.kuleuven.cs.distrinet.chameleon.ui.widget.checkbox;

import be.kuleuven.cs.distrinet.chameleon.ui.widget.PredicateSelector;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.SelectionController;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.WidgetFactory;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.True;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public class CheckboxSelector<T> extends PredicateSelector<T> {

	public CheckboxSelector(UniversalPredicate<? super T, Nothing> predicate, String message) {
		this(predicate,message,false);
	}
	
	public CheckboxSelector(UniversalPredicate<? super T, Nothing> predicate, String message, boolean initialValue) {
		_predicate = predicate;
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
	
	@Override
	public UniversalPredicate<? super T, Nothing> predicate() {
		if(_selection) {
			return _predicate;
		} else {
			return new True();
		}
	}

	private UniversalPredicate<? super T,Nothing> _predicate;

	private String _message;

	/**
	 * Does nothing because a checkbox selector is not context-sensitive.
	 */
	@Override
	public void setContext(Object context) {
	}
}
