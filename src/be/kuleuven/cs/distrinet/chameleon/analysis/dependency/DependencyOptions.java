package be.kuleuven.cs.distrinet.chameleon.analysis.dependency;

import be.kuleuven.cs.distrinet.chameleon.analysis.AbstractAnalysisOptions;
import be.kuleuven.cs.distrinet.chameleon.analysis.Result;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;

public abstract class DependencyOptions<E extends Element, R extends Result<R>> extends AbstractAnalysisOptions<E, R>{

	@Override
	public abstract DependencyResult createAnalysis();
}
