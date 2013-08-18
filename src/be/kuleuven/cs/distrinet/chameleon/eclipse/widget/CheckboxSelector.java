package be.kuleuven.cs.distrinet.chameleon.eclipse.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.True;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public class CheckboxSelector<T> extends PredicateSelector<T, Button>{

	public CheckboxSelector(UniversalPredicate<? super T, Nothing> predicate, String message) {
		_predicate = predicate;
		_message = message;
	}
	
	@Override
	public Button createControl(Composite parent) {
		if(_button == null) {
			_button = new Button(parent, SWT.CHECK);
			_button.setText(_message);
			_button.setSelection(true);
			_button.setEnabled(true);
			_button.setVisible(true);
		}
		return _button;
	}
	
	Button _button;

	@Override
	public UniversalPredicate<? super T, Nothing> predicate() {
		if(_button.getSelection()) {
			return _predicate;
		} else {
			return new True();
		}
	}

	private UniversalPredicate<? super T,Nothing> _predicate;

	private String _message;
}
