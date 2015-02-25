package org.aikodi.chameleon.aspect.core.model.aspect;

import java.util.List;

import org.aikodi.chameleon.aspect.core.model.advice.Advice;
import org.aikodi.chameleon.aspect.core.model.pointcut.Pointcut;
import org.aikodi.chameleon.core.declaration.BasicDeclaration;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.declaration.SimpleNameSignature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupContextFactory;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.core.scope.ScopeProperty;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.chameleon.util.association.Single;

public class Aspect extends BasicDeclaration implements DeclarationContainer, Declaration {
	
	public Aspect(String name) {
		super(name);
	}
	
	@Override
   public String name() {
		return signature().name();
	}
	
	/**
	 * 	Get the list of pointcuts that have been defined in this Aspect
	 */
	public List<Pointcut> pointcuts() {
		return _pointcuts.getOtherEnds();
	}
	
	private Multi<Pointcut> _pointcuts = new Multi<Pointcut>(this);
	
	public void addPointcut(Pointcut e) {
		add(_pointcuts, e);
	}

	
	/**
	 * 	Get the list of advices that have been defined in this Aspect
	 */
	public List<Advice> advices() {
		return _advices.getOtherEnds();
	}
	
	private Multi<Advice> _advices = new Multi<Advice>(this);
	
	public void addAdvice(Advice e) {
		add(_advices, e);
	}

	@Override
   public Aspect cloneSelf() {
		return new Aspect(name());
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	@Override
	public List<? extends Declaration> declarations() throws LookupException {
		return pointcuts();
	}

	@Override
	public List<? extends Declaration> locallyDeclaredDeclarations()
			throws LookupException {
		return declarations();
	}

	@Override
	public <D extends Declaration> List<? extends SelectionResult> declarations(
			DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public LookupContext lookupContext(Element child) throws LookupException {
		return language().lookupFactory().createLexicalLookupStrategy(localLookupStrategy(), this);
	}

	private LookupContext localLookupStrategy() {
		LookupContextFactory lookupFactory = language().lookupFactory();
		return lookupFactory.createLocalLookupStrategy(this);
	}
	
	@Override
	public LookupContext localContext() throws LookupException {
		return localLookupStrategy();
	}
	
	private Single<SimpleNameSignature> _signature = new Single<SimpleNameSignature>(this);

	@Override
	public SimpleNameSignature signature() {
		return _signature.getOtherEnd();
	}

	@Override
	public void setSignature(Signature signature) {
		if (!(signature instanceof SimpleNameSignature))
			throw new ChameleonProgrammerException("Exptected simpleNameSignature, got " + signature);
		
		set(_signature, (SimpleNameSignature) signature);
	}


	@Override
	public Declaration selectionDeclaration()
			throws LookupException {
		return this;
	}

	@Override
	public Declaration actualDeclaration() throws LookupException {
		return this;
	}

	@Override
	public Declaration declarator() {
		return this;
	}

	@Override
	public Scope scope() throws ModelException {
		//FIXME Duplicated from MemberImpl
  	Scope result = null;
  	ChameleonProperty scopeProperty = property(language().SCOPE_MUTEX());
  	if(scopeProperty instanceof ScopeProperty) {
  		result = ((ScopeProperty)scopeProperty).scope(this);
  	} else if(scopeProperty != null){
  		throw new ChameleonProgrammerException("Scope property is not a ScopeProperty");
  	}
  	return result;
	}

	@Override
	public void setName(String name) {
		setSignature(new SimpleNameSignature(name));
	}

	@Override
	public boolean complete() throws LookupException {
		return true;
	}

	@Override
	public LookupContext targetContext() throws LookupException {
		return localLookupStrategy();
	}
	
	@Override
	public Declaration finalDeclaration() {
		return this;
	}

	@Override
	public SelectionResult updatedTo(Declaration declaration) {
		return declaration;
	}
}
