package org.aikodi.chameleon.eclipse.view.dependency;
//package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;
//
//import java.util.List;
//
//import be.kuleuven.cs.distrinet.chameleon.analysis.Analysis;
//import be.kuleuven.cs.distrinet.chameleon.analysis.AnalysisOptions;
//import be.kuleuven.cs.distrinet.chameleon.analysis.OptionGroup;
//
//public class DependencyOptions implements AnalysisOptions {
//
//	
//	private OptionGroup _sourceOptions;
//
//	@Override
//	public List<OptionGroup> optionGroups() {
//		todo
//	}
//
//	@Override
//	public Analysis createAnalysis() {
//		todo
//	}
//	
////	public DependencyOptions(
////			List<PredicateSelector<? super Element>> sourceOptions,
////			List<PredicateSelector<? super Element>> crossReferenceOptions,
////			List<PredicateSelector<? super Element>> targetOptions,
////			List<PredicateSelector<? super Dependency<? super Element, ? super CrossReference, ? super Declaration>>> dependencyOptions,
////			Function<? super Element,? super Element,Nothing> mapper
////			) {
////		_sourceOptions = ImmutableList.copyOf(sourceOptions);
////		_crossReferenceOptions = ImmutableList.copyOf(crossReferenceOptions);
////		_targetOptions = ImmutableList.copyOf(targetOptions);
////		_dependencyOptions = ImmutableList.copyOf(dependencyOptions);
////		_mapper = mapper;
////	}
//	
////	public Function<? super Element,? super Element,Nothing> mapper() {
////		return _mapper;
////	}
////	private Function<? super Element,? super Element,Nothing> _mapper;
////	
////	public List<PredicateSelector<? super Element>> sourceOptions() {
////		return _sourceOptions;
////	}
////
////	public List<PredicateSelector<? super Element>> targetOptions() {
////		return _targetOptions;
////	}
////	
////	public List<PredicateSelector<? super Element>> crossReferenceOptions() {
////		return _crossReferenceOptions;
////	}
////	
////	public List<PredicateSelector<? super Dependency<? super Element, ? super CrossReference, ? super Declaration>>> dependencyOptions() {
////		return _dependencyOptions;
////	}
////
////
////	private ImmutableList<PredicateSelector<? super Element>> _sourceOptions;
////	
////	private ImmutableList<PredicateSelector<? super Element>> _crossReferenceOptions;
////
////	private ImmutableList<PredicateSelector<? super Element>> _targetOptions;
////
////	private ImmutableList<PredicateSelector<? super Dependency<? super Element, ? super CrossReference, ? super Declaration>>> _dependencyOptions;
////}
