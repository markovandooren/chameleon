package chameleon.core.lookup;

import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
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
   * Return the of the declaration that will be selected by this declaration selector.
   * 
   * This method is provided here such that declaration containers can keep a hashmap as
   * a cache, and quickly select the potential candidate(s). This way, we avoid having to
   * check all members. The most discriminating property of a declaration is usually its name.
   * 
   * @assumption We assume that a declaration selector can only select a declaration with a single
   *             name. If that is no longer the case, this method should return a collection of names.
   *             That makes the caching code more complex, and is slower, so we don't do that for now.
   * 
   * @return
   * @throws LookupException 
   */
  public abstract String selectionName(DeclarationContainer<?> container) throws LookupException;
  
  /**
   * Return the declarations of the given declaration container to which selection is applied.
   * This round-trip allows both the container and the selector to filter the candidates.
   * 
   * The default result is container.declarations(this), but clients cannot rely on that.
   */
  public List<D> declarations(DeclarationContainer container) throws LookupException {
  	return container.declarations(this);
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
  protected D actualDeclaration(Declaration declarator) throws LookupException {
  	Declaration<?, ?> declaration = declarator.selectionDeclaration();
		Declaration actualDeclaration = declaration.actualDeclaration();
		if(selectedClass().isInstance(actualDeclaration)) {
			return (D) actualDeclaration;
		} else {
			throw new LookupException("The actual declaration is of type "+actualDeclaration.getClass().getName()+" but a declaration of type "+selectedClass().getName()+" was expected.");
		}
  }
  
  /**
   * Required because 'instanceof D' cannot be used due to type erasure.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  protected abstract Class<D> selectedClass();
  
  /**
   * Return the list of declarations in the given set that are selected.
   * 
   * @param declarators
   *        The list containing the declarations that are checked for a match with {@link #selects(Signature)}}.
   * @return
   * @throws LookupException
   */
  public abstract List<D> selection(List<? extends Declaration> declarators) throws LookupException;
  
  /**
   * Return the list of declarations in the given set that are selected.
   * 
   * @param selectionCandidates
   *        The list containing the declarations that are checked for a match with {@link #selects(Signature)}}.
   * @return
   * @throws LookupException
   */
  public abstract List<? extends Declaration> declarators(List<? extends Declaration> selectionCandidates) throws LookupException;

  /**
   * Return the order used to sort the possible candidates. More specific elements should be smaller than less specific elements.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  protected abstract WeakPartialOrder<D> order();

	protected void applyOrder(List<D> tmp) throws LookupException {
		order().removeBiggerElements(tmp);
	}

	/**
	 * If the selectionName() of this selector must match declaration.signature().name() when that declaration is selected,
	 * then this method returns true. Otherwise, the method returns false. This method can be used for a String based preselection
	 * such that declaration containers can cache their declarations in a map with the declaration.signature().name() as the key.
	 * 
	 * For super constructor calls in Java, e.g. this method will return false.
	 * @return
	 */
	public boolean usesSelectionName() {
		return true;
	}

}
