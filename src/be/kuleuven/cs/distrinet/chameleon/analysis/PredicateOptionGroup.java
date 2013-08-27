package be.kuleuven.cs.distrinet.chameleon.analysis;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.ui.widget.PredicateSelector;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;
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
	
	private List<PredicateSelector<?>> _predicateSelectors = new ArrayList<>();
	
	public UniversalPredicate predicate() {
		UniversalPredicate result = new True();
		for(PredicateSelector selector: _predicateSelectors) {
			result = result.and(selector.predicate());
		}
		return result;
	}
}