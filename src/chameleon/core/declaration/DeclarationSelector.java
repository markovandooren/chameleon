package chameleon.core.declaration;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chameleon.core.MetamodelException;
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
   
  /**
   * Check if this selector selects the given declaration 
   * @param signature
   * @return
   */
  public boolean selects(Declaration declaration) throws MetamodelException {
    return filter(declaration) != null;
  }
  
  public abstract D filter(Declaration declaration) throws MetamodelException;
  
  /**
   * Required because 'instanceof D' cannot be used due to type erasure.
   * @return
   */
  public abstract Class<D> selectedClass();
  
  /**
   * Return the list of declarations in the given set that are selected.
   * 
   * @param set
   *        The list containing the declarations that are checked for a match with {@link #selects(Signature)}}.
   * @return
   * @throws MetamodelException
   */
  public Set<D> selection(Set<Declaration> set) throws MetamodelException {
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
    } catch(MetamodelException exc) {
      throw exc;
    } catch (Exception e) {
      e.printStackTrace();
      throw new Error("signature selection throws checked exception other than MetamodelException. This should not happen!");
    }
    return (Set<D>) tmp;
  }
  
  public abstract WeakPartialOrder<D> order();

}
