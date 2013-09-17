package be.kuleuven.cs.distrinet.chameleon.analysis;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.ui.widget.PredicateSelector;
import be.kuleuven.cs.distrinet.chameleon.util.Lists;
import be.kuleuven.cs.distrinet.rejuse.predicate.True;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

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