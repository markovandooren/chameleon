package chameleon.core.method;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.method.exception.ExceptionClause;
import chameleon.core.type.TypeReference;

public class MethodAlias extends Method<MethodAlias,MethodSignature> {

	public MethodAlias(MethodSignature signature, Method aliasedMethod) {
		super(signature);
		_aliasedMethod = aliasedMethod;
	}
	
	private Method _aliasedMethod;
	
	public Method aliasedMethod() {
		return _aliasedMethod;
	}

	@Override
	protected MethodAlias cloneThis() {
		return new MethodAlias(signature().clone(),aliasedMethod());
	}

	@Override
	public ExceptionClause getExceptionClause() {
		return aliasedMethod().getExceptionClause();
	}

	@Override
	public Implementation getImplementation() {
		return aliasedMethod().getImplementation();
	}

	@Override
	public TypeReference getReturnTypeReference() {
		return aliasedMethod().getReturnTypeReference();
	}

	@Override
	public boolean sameKind(Method other) {
		return aliasedMethod().sameKind(other);
	}

	@Override
	public void setExceptionClause(ExceptionClause clause) {
		throw new ChameleonProgrammerException("Trying to set the exception clause of a method alias.");
	}

	@Override
	public void setImplementation(Implementation implementation) {
		throw new ChameleonProgrammerException("Trying to set the implementation of a method alias.");
	}

}
