package org.aikodi.chameleon.aspect.core.model.pointcut;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Name;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.util.association.Single;

public class PointcutWithName extends Pointcut {

	private Single<Name> _signature = new Single<Name>(this);
	
	public PointcutWithName(Name signature, PointcutExpression expression) {
		super(expression);
		setSignature(signature);
	}

	@Override
	public boolean sameSignatureAs(Declaration declaration)
			throws LookupException {
		return signature().sameAs(declaration.signature());
	}

	@Override
	public Name signature() {
		return _signature.getOtherEnd();
	}

	@Override
	public void setSignature(Signature signature) {
		if(!(signature instanceof Name)) {
			throw new ChameleonProgrammerException();
		}
		set(_signature, (Name)signature);
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
