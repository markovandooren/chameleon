package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.analysis.predicate.IsSource;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.widget.CheckboxSelector;
import be.kuleuven.cs.distrinet.chameleon.eclipse.widget.PredicateSelector;
import be.kuleuven.cs.distrinet.chameleon.plugin.LanguagePluginImpl;

public class DefaultDependencyOptions extends LanguagePluginImpl implements DependencyOptions {

	@Override
	public List<PredicateSelector<? super Element, ?>> sourceOptions() {
		List list = new ArrayList();
		addSourceOnly(list);
		return list;
	}

	@Override
	public LanguagePluginImpl clone() {
		return new DefaultDependencyOptions();
	}

	@Override
	public List<PredicateSelector<? super Element, ?>> targetOptions() {
		List list = new ArrayList();
		addSourceOnly(list);
		return list;
	}

	private void addSourceOnly(List list) {
		list.add(new CheckboxSelector<>(new IsSource(), "Only source declarations"));
	}

}
