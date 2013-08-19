package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.widget.PredicateSelector;

import com.google.common.collect.ImmutableList;

public class DependencyConfiguration {

	public DependencyConfiguration(
			List<PredicateSelector<? super Element>> sourceOptions,
			List<PredicateSelector<? super Element>> targetOptions,
			List<PredicateSelector<? super Element>> dependencyOptions
			) {
		_sourceOptions = ImmutableList.copyOf(sourceOptions);
		_targetOptions = ImmutableList.copyOf(targetOptions);
		_dependencyOptions = ImmutableList.copyOf(dependencyOptions);
	}
	
	public List<PredicateSelector<? super Element>> sourceOptions() {
		return _sourceOptions;
	}

	public List<PredicateSelector<? super Element>> targetOptions() {
		return _targetOptions;
	}
	
	public List<PredicateSelector<? super Element>> dependencyOptions() {
		return _dependencyOptions;
	}


	private ImmutableList<PredicateSelector<? super Element>> _sourceOptions;

	private ImmutableList<PredicateSelector<? super Element>> _targetOptions;

	private ImmutableList<PredicateSelector<? super Element>> _dependencyOptions;
}
