package org.aikodi.chameleon.analysis.dependency;

import org.aikodi.chameleon.analysis.AbstractAnalysisOptions;
import org.aikodi.chameleon.analysis.Result;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.rejuse.action.Nothing;
import org.aikodi.rejuse.predicate.UniversalPredicate;

public abstract class DependencyOptions<E extends Element, R extends Result<R>> extends AbstractAnalysisOptions<E, R>{

	@Override
	public abstract DependencyResult analyze();
	
  public static final UniversalPredicate<Dependency, Nothing> IGNORE_LEXICAL_ANCESTORS = UniversalPredicate.of(Dependency.class, 
      t ->! ((Element) t.source()).hasAncestor((Element)t.target()));
  

}
