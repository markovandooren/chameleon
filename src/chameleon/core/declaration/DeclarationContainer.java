package chameleon.core.declaration;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;

/**
 * A general interface for elements that contain declarations. This interface allows the
 * class TargetContext to perform a local search. The declarations() method defines which
 * declarations are locally defined, after which the other objects involved in a lookup can 
 * decide if the request element is defined by the current declaration container or not.
 * 
 * @author Marko van Dooren
 *
 * @param <E>
 * @param <P>
 */
public interface DeclarationContainer<E extends DeclarationContainer, P extends Element> extends Element<E,P> {
  
  /**
   * Return the declarations the are defined in this declaration container.
   * @return
   * @throws LookupException 
   */
  public List<? extends Declaration> declarations() throws LookupException;
  
  /**
   * Return the declarations the are defined in this declaration container and selected
   * by the given declaration selector.
   * @return
   * @throws LookupException 
   */
 /*@
   @ public behavior
   @
   @ post \result.equals(selector.selection(declarations()));
   @*/
  public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException;
  
}
