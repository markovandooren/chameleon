package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.util.Lists;

public abstract class SelectorWithoutOrder<D extends Declaration> extends DeclarationSelector<D> {

	/**
	 * Return the signature that is used by this selector for selecting declarations.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public abstract Signature signature();
	
	/**
	 * The selection name is the name of the signature.
	 */
 /*@
   @ public behavior@/
   @
   @ post \result == signature().name();
   @*/
	@Override
	public String selectionName(DeclarationContainer container) {
		return signature().name();
	}

	/**
	 * Returns the selected declaration in case of a match.
	 * 
	 * @param declarator The declaration that declares the selected declaration.
	 * @return
	 * @throws LookupException
	 */
 /*@
   @ public behavior
   @
   @ pre declarator != null;
   @
   @ post \result == null || \result.sameAs(declarator.selectionDeclaration().actualDeclaration());
   @ post ! isCorrectType(declarator.selectionDeclaration()) ==> \result == null;
   @ post declarator.signature() == null ==> \result == null;
   @ post ! declarator.signature().sameAs(signature) ==> \result == null;
   @*/
  protected SelectionResult selection(Declaration declarator) throws LookupException {
  	// We first perform the checks on the selectionDeclaration, since a signature check may be
  	// very expensive.
  	D result = null;
  	Signature signature = declarator.signature();
		if(signature!=null && signature.sameAs(signature())) {
  		Declaration selectionDeclaration = declarator.selectionDeclaration();
  		if(hasSelectableType(selectionDeclaration)) {
  			result = (D) selectionDeclaration.actualDeclaration();
  		}
  	}
  	return result;
  }
  
  @Override
  public List<? extends SelectionResult> selection(List<? extends Declaration> declarators) throws LookupException {
  	List<SelectionResult> tmp = Lists.create();
  	for(Declaration decl: declarators) {
  		SelectionResult e = selection(decl);
  		if(e != null) {
  			tmp.add(e);
  		}
  	}
    return tmp;
  }

  /**
   * Return the list of declarations in the given set that are selected.
   * 
   * @param selectionCandidates
   *        The list containing the declarations that are checked for a match with {@link #selects(Signature)}}.
   * @return
   * @throws LookupException
   */
  public List<? extends SelectionResult> declarators(List<? extends Declaration> selectionCandidates) throws LookupException {
  	List<SelectionResult> result = Lists.create();
  	for(Declaration selectionCandidate: selectionCandidates) {
  		Signature signature = selectionCandidate.signature();
			if(signature!=null && signature.sameAs(signature())) {
  			Declaration selectionDeclaration = selectionCandidate.selectionDeclaration();
  			if(hasSelectableType(selectionDeclaration)) {
  				result.add(selectionCandidate.declarator());
  			}
  		} 
  	}
  	return result;
  }

  /**
   * Check whether the type of the given declaration is selected by this selector.
   * @param selectionDeclaration
   * @return
   */
	protected abstract boolean hasSelectableType(Declaration selectionDeclaration);

}
