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
   * This method decides which declarations are candidates for selection PROVIDED THAT IT IS OF THE CORRECT
   * CLASS. The method returns the given declaration if it is a candidate. The method returns
   * null if the given declaration is not a candidate.
   */
 /*@
   @ public behavior
   @
   @ pre selectedClass().isInstance(declaration);
   @ post \result == declaration | \result == null;
   @*/
  public abstract Declaration<?,?,?,D> filter(Declaration declaration) throws LookupException;

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
  	Declaration resolved = declarator.selectionDeclaration();
  	if(selectedClass().isInstance(resolved)) {
  	  Declaration<?, ?, ?, D> filtered = filter(resolved);
  	  if(filtered != null) {
  	  	// return filtered.actualDeclaration();
			  return actualDeclaration(declarator);
  	  } else {
  	  	return null;
  	  }
  	} else {
  		return null;
  	}
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
