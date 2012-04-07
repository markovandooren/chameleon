package chameleon.core.declaration;

import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.scope.Scope;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ModelException;
import chameleon.util.association.Single;

public class DeclarationAlias extends ElementImpl implements Declaration {

	public DeclarationAlias(Signature signature, Declaration declaration) {
		setSignature(signature);
 		_aliasedDeclaration = declaration;
	}
	
	@Override
	public DeclarationAlias clone() {
		return new DeclarationAlias(signature().clone(), aliasedDeclaration());
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

	public Declaration aliasedDeclaration() {
		return _aliasedDeclaration;
	}

	public Declaration actualDeclaration() {
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
		Declaration aliasedDeclaration = aliasedDeclaration();
		if(aliasedDeclaration != null) {
		  return aliasedDeclaration.scope();
		} else {
			throw new ModelException("The scope of the alias cannot be determined because the aliased declaration is null.");
		}
	}

	public Declaration selectionDeclaration() throws LookupException {
		return aliasedDeclaration();
	}

	public void setSignature(Signature signature) {
		set(_signature, signature);
	}
	
	public void setName(String name) {
		setSignature(new SimpleNameSignature(name));
	}
	
	public Signature signature() {
		return _signature.getOtherEnd();
	}

	private Single<Signature> _signature = new Single<Signature>(this); 

	private Declaration _aliasedDeclaration;

	public Declaration declarator() {
		return aliasedDeclaration().declarator();
	}

	@Override
	public boolean complete() throws LookupException {
		return aliasedDeclaration().complete();
	}

	@Override
	public LookupStrategy targetContext() throws LookupException {
		return aliasedDeclaration().targetContext();
	}

	@Override
	public String name() {
		return signature().name();
	}
}
