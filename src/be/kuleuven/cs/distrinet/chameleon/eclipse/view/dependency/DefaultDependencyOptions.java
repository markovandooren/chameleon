package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.analysis.predicate.IsSource;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.widget.CheckboxSelector;
import be.kuleuven.cs.distrinet.chameleon.eclipse.widget.PredicateSelector;
import be.kuleuven.cs.distrinet.chameleon.plugin.LanguagePluginImpl;

public class DefaultDependencyOptions extends LanguagePluginImpl implements DependencyOptions {

	@Override
	public DependencyConfiguration createConfiguration() {
		List<PredicateSelector<? super Element>> source = new ArrayList<>();
		source.add(onlySource());
		List<PredicateSelector<? super Element>> target = new ArrayList<>();
		target.add(onlySource());
		return new DependencyConfiguration(source, target, Collections.EMPTY_LIST,Collections.EMPTY_LIST);
	}

	private CheckboxSelector<Element> onlySource() {
		return new CheckboxSelector<>(new IsSource(), "Only source declarations");
	}

	@Override
	public LanguagePluginImpl clone() {
		return new DefaultDependencyOptions();
	}

}
