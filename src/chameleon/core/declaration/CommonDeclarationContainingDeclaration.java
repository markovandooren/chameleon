package chameleon.core.declaration;

import java.util.Collections;
import java.util.List;

import org.rejuse.predicate.TypePredicate;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.util.association.Multi;
import chameleon.util.association.Single;

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
	public LookupStrategy targetContext() throws LookupException {
		return language().lookupFactory().createLocalLookupStrategy(this);
	}

	@Override
	public List<? extends Declaration> declarations() {
		return _declarations.getOtherEnds();
	}
	
	public <D extends Declaration> List<D> declarations(Class<D> kind) {
		return (List<D>) new TypePredicate(kind).filterReturn(declarations());
	}
	
	@Override
	public abstract CommonDeclarationContainingDeclaration clone();
	
	@Override
	public LookupStrategy lexicalLookupStrategy(Element child) throws LookupException {
		if(childrenNotInScopeOfDeclarations().contains(child)) {
			return parent().lexicalLookupStrategy(this);
		} else {
			return super.lexicalLookupStrategy(child);
		}
	}
	

}
