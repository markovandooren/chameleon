package org.aikodi.chameleon.core.declaration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.util.association.Multi;

public class DeclarationContainerAlias extends ElementImpl implements DeclarationContainer {

	public DeclarationContainerAlias(DeclarationContainer parent) {
		setUniParent(parent);
	}
	
	
	public DeclarationContainerAlias() {
		
	}
	
	@Override
	public DeclarationContainerAlias cloneSelf() {
		return new DeclarationContainerAlias();
	}

	@Override
	public LookupContext lookupContext(Element child) throws LookupException {
		return parent().lookupContext(child);
	}


	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
		if(parent() == null) {
			result = result.and(new BasicProblem(this, "The declaration container alias does not alias any declaration."));
		}
		return result;
	}
	
	@Override
   public List<Declaration> declarations() {
		return _elements.getOtherEnds();
	}
	
	public List<Declaration> allDeclarations() {
		List<Declaration> result = Lists.create();
		for(DeclarationContainerAlias superContainer: superContainers()) {
			result.addAll(superContainer.allDeclarations());
		}
		result.addAll(declarations());
		return result;
	}

	@Override
   public <D extends Declaration> List<? extends SelectionResult<D>> declarations(DeclarationSelector<D> selector) throws LookupException {
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
	
	private List<DeclarationContainerAlias> _superContainers = Lists.create();

	@Override
   public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return _elements.getOtherEnds();
	}


	@Override
	public LookupContext localContext() throws LookupException {
		return ((DeclarationContainer)parent()).localContext();
	}
	
}
