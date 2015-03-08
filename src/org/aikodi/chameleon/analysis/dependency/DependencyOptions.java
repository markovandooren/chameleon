package org.aikodi.chameleon.analysis.dependency;

import org.aikodi.chameleon.analysis.AbstractAnalysisOptions;
import org.aikodi.chameleon.analysis.Result;
import org.aikodi.chameleon.core.element.Element;

public abstract class DependencyOptions<E extends Element, R extends Result<R>> extends AbstractAnalysisOptions<E, R>{

	@Override
	public abstract DependencyResult analyze();
}
