package be.kuleuven.cs.distrinet.chameleon.aspect.oo.weave.infrastructure;

import be.kuleuven.cs.distrinet.chameleon.aspect.oo.weave.factory.OOFactory;
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
		return factory().getAdvice().language().plugin(OOFactory.class);
	}

	private ExpressionInfrastructureFactory _factory;


}
