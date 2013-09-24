package be.kuleuven.cs.distrinet.chameleon.aspect.oo.weave.infrastructure;

import be.kuleuven.cs.distrinet.chameleon.aspect.oo.weave.factory.OOFactory;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;

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
