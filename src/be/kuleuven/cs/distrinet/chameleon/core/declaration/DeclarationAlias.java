package be.kuleuven.cs.distrinet.chameleon.core.declaration;

import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public class DeclarationAlias extends ElementImpl implements Declaration {

	public DeclarationAlias(Signature signature, Declaration declaration) {
		setSignature(signature);
 		_aliasedDeclaration = declaration;
	}
	
	@Override
	public boolean sameSignatureAs(Declaration declaration)
			throws LookupException {
		return signature().sameAs(declaration.signature());
	}
	
	@Override
	public DeclarationAlias cloneSelf() {
		return new DeclarationAlias(null, aliasedDeclaration());
	}

	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
		if(aliasedDeclaration() == null) {
			result = result.and(new BasicProblem(this, "The alias with signature "+name()+" does not alias any declaration."));
		}
		if(name() == null) {
			result = result.and(new BasicProblem(this, "The signature of the alias is null, but it should be non-null."));
		}
		return result;
	}

	public Declaration aliasedDeclaration() {
		return _aliasedDeclaration;
	}

	@Override
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
	@Override
   public Scope scope() throws ModelException {
		Declaration aliasedDeclaration = aliasedDeclaration();
		if(aliasedDeclaration != null) {
		  return aliasedDeclaration.scope();
		} else {
			throw new ModelException("The scope of the alias cannot be determined because the aliased declaration is null.");
		}
	}

	@Override
   public Declaration selectionDeclaration() throws LookupException {
		return aliasedDeclaration();
	}

	@Override
   public void setSignature(Signature signature) {
		set(_signature, signature);
	}
	
	@Override
   public void setName(String name) {
		setSignature(new SimpleNameSignature(name));
	}
	
	@Override
   public Signature signature() {
		return _signature.getOtherEnd();
	}

	private Single<Signature> _signature = new Single<Signature>(this); 

	private Declaration _aliasedDeclaration;

	@Override
   public Declaration declarator() {
		return aliasedDeclaration().declarator();
	}

	@Override
	public boolean complete() throws LookupException {
		return aliasedDeclaration().complete();
	}

	@Override
	public LookupContext targetContext() throws LookupException {
		return aliasedDeclaration().targetContext();
	}

	@Override
	public String name() {
		return signature().name();
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
