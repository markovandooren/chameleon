package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.Dependency;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.PredicateSelector;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.function.Function;

import com.google.common.collect.ImmutableList;

public class DependencyConfiguration {

	public DependencyConfiguration(
			List<PredicateSelector<? super Element>> sourceOptions,
			List<PredicateSelector<? super Element>> crossReferenceOptions,
			List<PredicateSelector<? super Element>> targetOptions,
			List<PredicateSelector<? super Dependency<? super Element, ? super CrossReference, ? super Declaration>>> dependencyOptions,
			Function<? super Element,? super Element,Nothing> mapper
			) {
		_sourceOptions = ImmutableList.copyOf(sourceOptions);
		_crossReferenceOptions = ImmutableList.copyOf(crossReferenceOptions);
		_targetOptions = ImmutableList.copyOf(targetOptions);
		_dependencyOptions = ImmutableList.copyOf(dependencyOptions);
		_mapper = mapper;
	}
	
	public Function<? super Element,? super Element,Nothing> mapper() {
		return _mapper;
	}
	private Function<? super Element,? super Element,Nothing> _mapper;
	
	public List<PredicateSelector<? super Element>> sourceOptions() {
		return _sourceOptions;
	}

	public List<PredicateSelector<? super Element>> targetOptions() {
		return _targetOptions;
	}
	
	public List<PredicateSelector<? super Element>> crossReferenceOptions() {
		return _crossReferenceOptions;
	}
	
	public List<PredicateSelector<? super Dependency<? super Element, ? super CrossReference, ? super Declaration>>> dependencyOptions() {
		return _dependencyOptions;
	}


	private ImmutableList<PredicateSelector<? super Element>> _sourceOptions;
	
	private ImmutableList<PredicateSelector<? super Element>> _crossReferenceOptions;

	private ImmutableList<PredicateSelector<? super Element>> _targetOptions;

	private ImmutableList<PredicateSelector<? super Dependency<? super Element, ? super CrossReference, ? super Declaration>>> _dependencyOptions;
}
