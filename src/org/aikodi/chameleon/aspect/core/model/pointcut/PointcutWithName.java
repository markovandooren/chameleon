package org.aikodi.chameleon.aspect.core.model.pointcut;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.declaration.SimpleNameSignature;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.util.association.Single;

public class PointcutWithName extends Pointcut {

	private Single<SimpleNameSignature> _signature = new Single<SimpleNameSignature>(this);
	
	public PointcutWithName(SimpleNameSignature signature, PointcutExpression expression) {
		super(expression);
		setSignature(signature);
	}

	@Override
	public boolean sameSignatureAs(Declaration declaration)
			throws LookupException {
		return signature().sameAs(declaration.signature());
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
	protected PointcutWithName cloneSelf() {
		return new PointcutWithName(null,null);
	}

	@Override
	public LocalLookupContext<?> targetContext() throws LookupException {
		throw new LookupException("Requesting the target context of a pointcut.");
	}

	@Override
	public String name() {
		return signature().name();
	}

}
