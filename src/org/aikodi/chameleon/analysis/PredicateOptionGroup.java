package org.aikodi.chameleon.analysis;

import java.util.List;

import org.aikodi.chameleon.ui.widget.PredicateSelector;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.rejuse.predicate.True;
import org.aikodi.rejuse.predicate.UniversalPredicate;

public class PredicateOptionGroup extends OptionGroup {

	public PredicateOptionGroup(String name) {
		super(name);
	}
	
	protected void addPredicateSelector(PredicateSelector<?> selector) {
		add(selector);
		_predicateSelectors.add(selector);
	}
	
	private List<PredicateSelector<?>> _predicateSelectors = Lists.create();
	
	public UniversalPredicate predicate() {
		return predicate(_predicateSelectors);
	}

	public UniversalPredicate predicate(List<PredicateSelector<?>> selectors) {
		UniversalPredicate result = new True();
		for(PredicateSelector selector: selectors) {
			result = result.and(selector.predicate());
		}
		return result;
	}
}