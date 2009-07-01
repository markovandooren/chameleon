package chameleon.core.lookup;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import chameleon.core.MetamodelException;
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
   @ post \result == (filter(declaration) != null);
   @*/
  public boolean selects(Declaration declaration) throws LookupException {
    return filter(declaration) != null;
  }
  
  /**
   * This method decides which declarations are candidates for selection. The
   * method returns the given declaration if it is a candidate. The method returns
   * null if the given declaration is not a candidate.
   */
 /*@
   @ public behavior
   @
   @ post \result == declaration | \result == null;
   @*/
  public abstract D filter(Declaration declaration) throws LookupException;
  
  /**
   * Required because 'instanceof D' cannot be used due to type erasure.
   * @return
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
   * @param set
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
  public Set<D> selection(Set<Declaration> set) throws LookupException {
    Set<Declaration> tmp = new HashSet<Declaration>();
    try {
      for(Declaration decl: set) {
        D e = filter(decl);
        if(e != null) {
          tmp.add(e);
        }
      }
      order().removeBiggerElements((Collection<D>) tmp);
    } catch(RuntimeException exc) {
      throw exc;
    } catch(Error err) {
      throw err;
    } catch(LookupException exc) {
      throw exc;
    } catch (Exception e) {
      e.printStackTrace();
      throw new Error("signature selection throws checked exception other than MetamodelException. This should not happen!");
    }
    return (Set<D>) tmp;
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
