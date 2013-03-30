package be.kuleuven.cs.distrinet.chameleon.core.declaration;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;

/**
 * A general interface for elements that contain declarations. This interface allows the
 * class TargetContext to perform a local search. The declarations() method defines which
 * declarations are locally defined, after which the other objects involved in a lookup can 
 * decide if the request element is defined by the current declaration container or not.
 * 
 * @author Marko van Dooren
 */
public interface DeclarationContainer extends Element {
  
  /**
   * Return the declarations the are defined in this declaration container.
   * The resulting collection contains the locally declared declarations and
   * those that this declaration container receives from other declaration
   * containers (e.g. through an inheritance relation).
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post \result.containsAll(locallyDeclaredDeclarations());
   @*/
  public List<? extends Declaration> declarations() throws LookupException;
  
  /**
   * Return the declarations the are defined locally in this declaration container.
   * @return
   * @throws LookupException
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException;
  
  /**
   * Return the declarations the are defined in this declaration container and selected
   * by the given declaration selector.
   * 
   * Most implementations will directly invoke selector.selection(declarations()), but in some cases, 
   * calculating the collection of declarations is very expensive. In such cases, the selector is typically
   * pass along the chain of objects that contain the declarations of this container. For example, a class will pass
   * the selector to its super classes instead of asking them for all declarations and then using the selector.
   * Applying the inheritance rules (such as overriding) to all class members is very expensive, and useless for
   * declarations that cannot be selected anyway.
   */
 /*@
   @ public behavior
   @
   @ post \result.equals(selector.selection(declarations()));
   @*/
  public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException;

	public LookupContext localContext() throws LookupException;
  
}
