package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.declaration.BasicDeclaration;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Name;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;

public abstract class Parameter extends BasicDeclaration {
	
	public Parameter(String name) {
		super(name);
	}
	
	@Override
   protected abstract Element cloneSelf();
	
	@Override
   public abstract Declaration selectionDeclaration() throws LookupException;
	
	public Class<Name> signatureType() {
		return Name.class;
	}
	
	@Override
	public Declaration finalDeclaration() {
		return this;
	}
	
	@Override
	public Declaration template() {
		return finalDeclaration();
	}

	@Override
	public SelectionResult updatedTo(Declaration declaration) {
		return declaration;
	}

}
