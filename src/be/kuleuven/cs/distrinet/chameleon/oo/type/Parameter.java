package be.kuleuven.cs.distrinet.chameleon.oo.type;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.BasicDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;

public abstract class Parameter extends BasicDeclaration {
	
	public Parameter(String name) {
		super(name);
	}
	
	protected abstract Element cloneSelf();
	
	public abstract Declaration selectionDeclaration() throws LookupException;
	
	public Class<SimpleNameSignature> signatureType() {
		return SimpleNameSignature.class;
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
