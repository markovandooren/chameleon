package be.kuleuven.cs.distrinet.chameleon.core.declaration;

import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.rejuse.predicate.TypePredicate;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public abstract class CommonDeclarationContainingDeclaration extends
		DeclarationContainingDeclarationImpl {

	public CommonDeclarationContainingDeclaration() {
		
	}
	
	public CommonDeclarationContainingDeclaration(Signature signature) {
		setSignature(signature);
	}
	
	private Single<Signature> _signature = new Single<Signature>(this);

	@Override
	public Signature signature() {
		return _signature.getOtherEnd();
	}

	@Override
	public void setSignature(Signature signature) {
		set(_signature,signature);
	}
	
	public List<Element> childrenNotInScopeOfDeclarations() {
		return Collections.EMPTY_LIST;
	}

	private Multi<Declaration> _declarations = new Multi<Declaration>(this);
	
	public void addDeclaration(Declaration d) {
		add(_declarations,d);
	}

	@Override
	public LookupContext targetContext() throws LookupException {
		return language().lookupFactory().createLocalLookupStrategy(this);
	}

	public List<? extends Declaration> localDeclarations() {
		return _declarations.getOtherEnds();
	}
	
	@Override
	public List<? extends Declaration> declarations() throws LookupException {
		return localDeclarations();
	}

	public <D extends Declaration> List<D> declarations(Class<D> kind) throws LookupException {
		return (List<D>) new TypePredicate(kind).filterReturn(declarations());
	}
	
	@Override
	public abstract CommonDeclarationContainingDeclaration clone();
	
	@Override
	public LookupContext lookupContext(Element child) throws LookupException {
		if(childrenNotInScopeOfDeclarations().contains(child)) {
			return parent().lookupContext(this);
		} else {
			return super.lookupContext(child);
		}
	}
	
  @Override
  public VerificationResult verifySelf() {
  	VerificationResult result = super.verifySelf();
  	try {
  	List<? extends Declaration> declarations = declarations();
  	for(Declaration first: declarations) {
  		for(Declaration second: declarations) {
  			if(first != second && first.signature().sameAs(second.signature())) {
  				result = result.and(new BasicProblem(first, "There is another declaration with the same signature defined in this container."));
  			}
  		}
  	}
  	} catch(LookupException exc) {
  		// Do nothing. If the computation of declarations fail, some other verification rule should report a problem.
  	}
  	return result;
  }
}
