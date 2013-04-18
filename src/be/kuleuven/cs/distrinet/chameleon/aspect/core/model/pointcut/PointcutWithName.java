package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public class PointcutWithName extends Pointcut {

	private Single<SimpleNameSignature> _signature = new Single<SimpleNameSignature>(this);
	
	public PointcutWithName(SimpleNameSignature signature, PointcutExpression expression) {
		super(expression);
		setSignature(signature);
	}

	@Override
	public SimpleNameSignature signature() {
		return _signature.getOtherEnd();
	}

	@Override
	public void setSignature(Signature signature) {
		if(!(signature instanceof SimpleNameSignature)) {
			throw new ChameleonProgrammerException();
		}
		set(_signature, (SimpleNameSignature)signature);
	}

	@Override
	public void setName(String name) {
		signature().setName(name);
	}

	@Override
	protected PointcutWithName cloneThis() {
		return new PointcutWithName(signature().clone(),expression());
	}

	@Override
	public LookupContext targetContext() throws LookupException {
		throw new LookupException("Requesting the target context of a pointcut.");
	}

	@Override
	public String name() {
		return signature().name();
	}

}
