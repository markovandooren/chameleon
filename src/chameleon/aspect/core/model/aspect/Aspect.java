package chameleon.aspect.core.model.aspect;

import java.util.List;

import org.rejuse.association.MultiAssociation;
import org.rejuse.association.OrderedMultiAssociation;

import chameleon.aspect.core.model.advice.Advice;
import chameleon.aspect.core.model.pointcut.Pointcut;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.LookupStrategyFactory;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.exception.ModelException;
import chameleon.util.association.Multi;
import chameleon.util.association.Single;

public class Aspect extends ElementImpl implements DeclarationContainer, Declaration {
	
	public Aspect(String name) {
		this(new SimpleNameSignature(name));
	}
	
	public Aspect(SimpleNameSignature sig) {
		setSignature(sig);
	}
	
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

	public Aspect clone() {
		Aspect clone = new Aspect(signature() == null ? null : signature().clone());
		
		for (Pointcut pc : pointcuts()) {
			Pointcut pcClone = pc.clone();
			clone.addPointcut(pcClone);
		}
		
		for (Advice ac : advices()) {
			Advice adviceClone = ac.clone();
			clone.addAdvice(adviceClone);
		}
		
		return clone;
	}

	@Override
	public VerificationResult verifySelf() {
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
	public <D extends Declaration> List<D> declarations(
			DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public LookupStrategy lexicalLookupStrategy(Element child) throws LookupException {
		return language().lookupFactory().createLexicalLookupStrategy(localLookupStrategy(), this);
	}

	private LookupStrategy localLookupStrategy() {
		LookupStrategyFactory lookupFactory = language().lookupFactory();
		return lookupFactory.createLocalLookupStrategy(this);
	}
	
	@Override
	public LookupStrategy localStrategy() throws LookupException {
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
	public LookupStrategy targetContext() throws LookupException {
		return localLookupStrategy();
	}
}