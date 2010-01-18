package chameleon.core.lookup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.relation.WeakPartialOrder;

/**
 * A class of objects that select declarations during lookup.
 * 
 * @author Marko van Dooren
 *
 * @param <D> The type of the declarations selected by this signature selector.
 *            This parameter allows for more specific typing of the result of {@see #selection(Set)},
 *            and consequently of {@see Context#declaration(SignatureSelector)}.
 */
public abstract class DeclarationSelector<D extends Declaration> {
   
	public DeclarationSelector() {
		// Only here to be able to query call hierarchy
	}
	
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
  public abstract boolean selectedRegardlessOfSignature(D declaration) throws LookupException;
  
  /**
   * This method decides if the given signature is selected by this declaration selector.
   */
 /*@
   @ public behavior
   @
   @ pre signature != null;
   @*/
  public abstract boolean selected(Signature signature) throws LookupException;

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
  public D selection(Declaration declarator) throws LookupException {
  	// We first perform the checks on the selectionDeclaration, since a signature check may be
  	// very expensive.
  	D result = null;
  	Declaration selectionDeclaration = declarator.selectionDeclaration();
  	if(selectedClass().isInstance(selectionDeclaration)) {
  	  if(selectedRegardlessOfSignature((D)selectionDeclaration)) {
  	  	if(selected(declarator.signature())) {
			    result = actualDeclaration(declarator);
  	  	}
  	  }
  	} 
    return result;
  }
  
  /**
   * Select the actual declaration selected by this declaration selector.
   * By default, this method returns declarator.selectionDeclaration()).actualDeclaration().
   * 
   * This method was introduced to allow the declarator to be returned instead (in DeclaratorSelector)
   * without having to add lookup method everywhere that would do the same as the original
   * lookup methods except for invoking a (currently non-existant) declarator() method instead
   * of selection().
   * 
   * @param declarator
   * @return
   * @throws LookupException
   */
  public D actualDeclaration(Declaration declarator) throws LookupException {
  	return ((Declaration<?, ?, ?, D>)declarator.selectionDeclaration()).actualDeclaration();
  }
  
  /**
   * Required because 'instanceof D' cannot be used due to type erasure.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public abstract Class<D> selectedClass();
  
  /**
   * Return the list of declarations in the given set that are selected.
   * 
   * @param selectionCandidates
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
  public List<D> selection(List<? extends Declaration> selectionCandidates) throws LookupException {
    List<Declaration> tmp = new ArrayList<Declaration>();
      for(Declaration decl: selectionCandidates) {
        D e = selection(decl);
        if(e != null) {
          tmp.add(e);
        }
      }
      order().removeBiggerElements((Collection<D>) tmp);
    return (List<D>) tmp;
  }
  
  /**
   * Return the order used to sort the possible candidates. More specific elements should be smaller than less specific elements.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public abstract WeakPartialOrder<D> order();

}
