package org.aikodi.chameleon.ui.widget.checkbox;

import org.aikodi.chameleon.ui.widget.CheckboxSelector;
import org.aikodi.chameleon.ui.widget.PredicateSelector;
import org.aikodi.rejuse.action.Nothing;
import org.aikodi.rejuse.predicate.UniversalPredicate;

public class CheckboxPredicateSelector<T> extends CheckboxSelector implements PredicateSelector<T> {

	public CheckboxPredicateSelector(UniversalPredicate<? super T, Nothing> predicate, String message) {
		this(predicate,message,false);
	}
	
	public CheckboxPredicateSelector(UniversalPredicate<? super T, Nothing> predicate, String message, boolean initialValue) {
		super(message,initialValue);
		_predicate = predicate;
	}

	@Override
	public UniversalPredicate<? super T, Nothing> predicate() {
		if(selected()) {
			return _predicate;
		} else {
			return UniversalPredicate.isTrue();
		}
	}

	private UniversalPredicate<? super T,Nothing> _predicate;

}
