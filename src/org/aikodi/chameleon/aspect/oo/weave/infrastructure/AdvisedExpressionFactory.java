package org.aikodi.chameleon.aspect.oo.weave.infrastructure;

import org.aikodi.chameleon.aspect.oo.weave.factory.OOFactory;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;

public class AdvisedExpressionFactory {
	
	public AdvisedExpressionFactory(ExpressionInfrastructureFactory factory) {
		if(factory == null) {
			throw new ChameleonProgrammerException();
		}
		_factory = factory;
	}

	public ExpressionInfrastructureFactory factory() {
		return _factory;
	}
	
	protected OOFactory ooFactory() {
		return language().plugin(OOFactory.class);
	}

	protected Language language() {
		return factory().getAdvice().language();
	}

	private ExpressionInfrastructureFactory _factory;


}
