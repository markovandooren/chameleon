package chameleon.core.variable;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.type.TypeReference;

public class VariableAlias extends Variable<VariableAlias,VariableContainer> {
	
	public VariableAlias(SimpleNameSignature sig, Variable aliasedVariable) {
		super(sig);
		_aliasedVariable = aliasedVariable;
	}
	
	public Variable aliasedVariable() {
		return _aliasedVariable;
	}
	
	private final Variable _aliasedVariable;

	@Override
	protected VariableAlias cloneThis() {
		return new VariableAlias(signature().clone(),aliasedVariable());
	}

	@Override
	public TypeReference getTypeReference() {
		return aliasedVariable().getTypeReference();
	}

}
