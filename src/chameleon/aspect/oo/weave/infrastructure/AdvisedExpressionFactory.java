package chameleon.aspect.oo.weave.infrastructure;

import chameleon.aspect.oo.weave.factory.OOFactory;
import chameleon.exception.ChameleonProgrammerException;

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
