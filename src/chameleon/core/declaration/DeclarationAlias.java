package chameleon.core.declaration;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.scope.Scope;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ModelException;

public class DeclarationAlias<D extends Declaration> extends NamespaceElementImpl<DeclarationAlias<D>> implements Declaration<DeclarationAlias<D>, Signature> {

	public DeclarationAlias(Signature signature, D declaration) {
		setSignature(signature);
 		_aliasedDeclaration = declaration;
	}
	
	@Override
	public DeclarationAlias clone() {
		return new DeclarationAlias<Declaration>(signature().clone(), aliasedDeclaration());
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
		if(aliasedDeclaration() == null) {
			result = result.and(new BasicProblem(this, "The alias with signature "+signature().toString()+" does not alias any declaration."));
		}
		if(signature() == null) {
			result = result.and(new BasicProblem(this, "The signature of the alias is null, but it should be non-null."));
		}
		return result;
	}

	public D aliasedDeclaration() {
		return _aliasedDeclaration;
	}

	public D actualDeclaration() {
		return _aliasedDeclaration;
	}

	/**
	 * Return the scope of the aliased declaration. Since an alias is just another name for a declaration,
	 * the scope does not change.
	 */
 /*@
   @ public behavior
   @
   @ post \result == actualDeclaration.scope();
   @
   @ signals(MetamodelException) actualDeclaration() == null;
   @*/
	public Scope scope() throws ModelException {
		D aliasedDeclaration = aliasedDeclaration();
		if(aliasedDeclaration != null) {
		  return aliasedDeclaration.scope();
		} else {
			throw new ModelException("The scope of the alias cannot be determined because the aliased declaration is null.");
		}
	}

	public Declaration<?, ?> selectionDeclaration() throws LookupException {
		return aliasedDeclaration();
	}

	public List<? extends Element> children() {
		return new ArrayList<Element>();
	}
	
	public void setSignature(Signature signature) {
		setAsParent(_signature, signature);
	}
	
	public void setName(String name) {
		setSignature(new SimpleNameSignature(name));
	}
	
	public Signature signature() {
		return _signature.getOtherEnd();
	}

	private SingleAssociation<DeclarationAlias, Signature> _signature = new SingleAssociation<DeclarationAlias, Signature>(this); 

	private D _aliasedDeclaration;

	public Declaration declarator() {
		return aliasedDeclaration().declarator();
	}

	@Override
	public boolean complete() throws LookupException {
		return aliasedDeclaration().complete();
	}
}
