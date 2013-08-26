package be.kuleuven.cs.distrinet.chameleon.analysis.dependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.analysis.predicate.IsSource;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency.DependencyConfiguration;
import be.kuleuven.cs.distrinet.chameleon.plugin.LanguagePluginImpl;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.PredicateSelector;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.checkbox.CheckboxSelector;
import be.kuleuven.cs.distrinet.rejuse.function.Function;

public class DefaultDependencyOptions extends LanguagePluginImpl implements DependencyOptions {

	@Override
	public DependencyConfiguration createConfiguration() {
		List<PredicateSelector<? super Element>> source = new ArrayList<>();
		List<PredicateSelector<? super Element>> target = new ArrayList<>();
		Function identity = new Function() {

			@Override
			public Object apply(Object argument) throws Exception {
				return argument;
			}
		};
		target.add(onlySource());
		return new DependencyConfiguration(source, Collections.EMPTY_LIST,target,Collections.EMPTY_LIST, identity);
	}

	public CheckboxSelector<Element> onlySource() {
		return new CheckboxSelector<>(new IsSource(), "Only source declarations");
	}

	@Override
	public LanguagePluginImpl clone() {
		return new DefaultDependencyOptions();
	}

}
