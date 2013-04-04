package be.kuleuven.cs.distrinet.chameleon.core.declaration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;

public class DeclarationContainerAlias extends ElementImpl implements DeclarationContainer {

	public DeclarationContainerAlias(DeclarationContainer parent) {
		setUniParent(parent);
	}
	
	
	public DeclarationContainerAlias() {
		
	}
	
	@Override
	public DeclarationContainerAlias clone() {
		DeclarationContainerAlias result = new DeclarationContainerAlias();
		for(Declaration declaration: declarations()) {
			Declaration clone = declaration.clone();
			clone.setOrigin(declaration.origin());
			result.add(clone);
		}
		for(DeclarationContainerAlias alias: superContainers()) {
			result.addSuperContainer(alias.clone());
		}
		result.setUniParent(parent());
		return result;
	}

	@Override
	public LookupContext lookupContext(Element child) throws LookupException {
		return parent().lookupContext(child);
	}


	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
		if(parent() == null) {
			result = result.and(new BasicProblem(this, "The declaration container alias does not alias any declaration."));
		}
		return result;
	}
	
	public List<Declaration> declarations() {
		return _elements.getOtherEnds();
	}
	
	public List<Declaration> allDeclarations() {
		List<Declaration> result = new ArrayList<Declaration>();
		for(DeclarationContainerAlias superContainer: superContainers()) {
			result.addAll(superContainer.allDeclarations());
		}
		result.addAll(declarations());
		return result;
	}

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	private Multi<Declaration> _elements = new Multi<Declaration>(this);

	public void add(Declaration element) {
	  add(_elements, element);
	}
	
	public void addAll(Collection<Declaration> elements) {
		for(Declaration declaration:elements) {
			add(declaration);
		}
	}
	
	public void remove(Declaration element) {
	  remove(_elements,element);
	}
	
	public List<DeclarationContainerAlias> superContainers() {
		return new ArrayList<DeclarationContainerAlias>(_superContainers);
	}
	
	public void addSuperContainer(DeclarationContainerAlias container) {
		_superContainers.add(container);
	}
	
	public void removeSuperContainer(DeclarationContainerAlias container) {
		_superContainers.remove(container);
	}
	
	private List<DeclarationContainerAlias> _superContainers = new ArrayList<DeclarationContainerAlias>();

	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return _elements.getOtherEnds();
	}


	@Override
	public LookupContext localContext() throws LookupException {
		return ((DeclarationContainer)parent()).localContext();
	}
	
}
