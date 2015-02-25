package org.aikodi.chameleon.core.lookup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.util.Lists;

public abstract class TwoPhaseDeclarationSelector<D extends Declaration> extends DeclarationSelector<D> {

  /**
   * This method decides which declarations are candidates for selection <b>provided that it is of the correct
   * type</b>. This method does not take the signature into account. That check is performed by the selected() method.
   * The reason to split this up is to support aliases without have to create a separate trio of 
   * interface, actualdeclaration, and aliasdeclaration for each declaration. The alias contains only the alias name. Further
   * checks are performed on the 'selectionDeclaration'. Since the selectionDeclaration still have the original signature, the
   * latter signature must of course be ignored.
   */
 /*@
   @ public behavior
   @
   @ pre selectedClass().isInstance(declaration);
   @*/
  public abstract boolean selectedRegardlessOfName(D declaration) throws LookupException;

  /**
   * Return the declaration of type D that would be selected based on the
   * given input declaration. 
   * 
   * First, selectionDeclaration() is invoked on the
   * input declaration, resulting in declaration 'd'. 
   * 
   * Second, 'd' is then passed to the filter()
   * method to see if it would be selected. 
   * 
   * Finally, if filter selects 'd', actualDeclaration()
   * is invoked to produce the actual declaration that is selected.
   * 
   * In most cases, both selectionDeclaration() and actualDeclaration() of the Declaration
   * will simply return the declaration on which they were invoked. For formal type parameters, and
   * actual type parameters, this is not the case.
   * 
   * @param declarator
   * @return
   * @throws LookupException
   */
 /*@
   @ public behavior
   @
   @ post \result == declaration | \result == null;
   @ post ! selectedClass().isInstance(declaration) ==> \result == null;
   @*/
  protected SelectionResult selection(Declaration declarator) throws LookupException {
  	// We first perform the checks on the selectionDeclaration, since a signature check may be
  	// very expensive.
  	SelectionResult result = null;
  	if(selectedBasedOnName(declarator.signature())) {
  		Declaration selectionDeclaration = declarator.selectionDeclaration();
  		if(selectedClass().isInstance(selectionDeclaration)) {
  			if(selectedRegardlessOfName((D)selectionDeclaration)) {
  				result = selectionDeclaration.actualDeclaration();
  			}
  		}
  	}
  	return result;
  }

  /**
   * Return the list of declarations in the given set that are selected.
   * 
   * @param declarators
   *        The list containing the declarations that are checked for a match with {@link #selects(Signature)}}.
   * @return
   * @throws LookupException
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post (\forall D d;; \result.contains(d) == 
   @           (set.contains(d) && 
   @            selects(d) && 
   @            ! (\exists D other; set.contains(other); order().strictOrder().contains(other,d))));
   @*/
  @Override
public List<? extends SelectionResult> selection(List<? extends Declaration> declarators) throws LookupException {
  	List<SelectionResult> tmp = Lists.create();
  	for(Declaration decl: declarators) {
  		SelectionResult e = selection(decl);
  		if(e != null) {
  			tmp.add(e);
  		}
  	}
  	applyOrder(tmp);
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
  @Override
public List<? extends SelectionResult> declarators(List<? extends Declaration> selectionCandidates) throws LookupException {
  	Map<D,Declaration> tmp = new HashMap<D,Declaration>();
  	List<D> Ds = Lists.create();
  	Class<D> selectedClass = selectedClass();
  	for(Declaration selectionCandidate: selectionCandidates) {
  		if(selectedBasedOnName(selectionCandidate.signature())) {
  			Declaration selectionDeclaration = selectionCandidate.selectionDeclaration();
  			if(selectedClass.isInstance(selectionDeclaration)) {
  				if(selectedRegardlessOfName((D)selectionDeclaration)) {
  					tmp.put((D) selectionDeclaration,selectionCandidate.declarator());
  					Ds.add((D) selectionDeclaration);
  				}
  			}
  		} 
  	}
  	applyOrder((List)Ds);
  	List<SelectionResult> result = Lists.create();
  	for(D d: Ds) {
  		result.add(tmp.get(d));
  	}
  	return result;
  }

	protected abstract void applyOrder(List<SelectionResult> tmp) throws LookupException;

  /**
   * Check if this selector selects the given declaration 
   * @param signature
   * @return
   */
 /*@
   @ public behavior
   @
   @ post \result == (selection(declaration) != null);
   @*/
  public boolean selects(Declaration declaration) throws LookupException {
    return selection(declaration) != null;
  }

  public abstract Class<D> selectedClass();
  
  @Override
  public boolean canSelect(Class<? extends Declaration> type) {
  	return selectedClass().isAssignableFrom(type);
  }
  
	/**
	 * Determine whether the declaration represented by the given signature is
	 * selected based on the name of the signature.
	 */
 /*@
   @ public behavior
   @
   @ pre signature != null;
   @*/
  public abstract boolean selectedBasedOnName(Signature signature) throws LookupException;
  

}
