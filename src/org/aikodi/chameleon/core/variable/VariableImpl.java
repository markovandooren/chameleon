package org.aikodi.chameleon.core.variable;

import org.aikodi.chameleon.core.declaration.BasicDeclaration;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.MissingSignature;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.type.Type;

public abstract class VariableImpl extends BasicDeclaration implements Variable {
	
	public VariableImpl(String name) {
		super(name);
	}
	
	@Override
   public Type declarationType() throws LookupException {
		return getType();
	}
	
	@Override
   public boolean complete() {
		return true;
	}
	

	@Override
	public Verification verifySelf() {
		if(name() != null) {
		  return Valid.create();
		} else {
			return new MissingSignature(this); 
		}
	}

	@Override
   public String toString() {
		return getTypeReference().toString() +" "+name();
	}
	
	@Override
	public SelectionResult updatedTo(Declaration declaration) {
		return declaration;
	}

}
