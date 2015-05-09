package org.aikodi.chameleon.core.declaration;

import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupContextFactory;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.util.Lists;

/**
 * An element that contains declarations. This interface allows the
 * class {@link LocalLookupContext} to perform a local search. The 
 * {@link #declarations()} method defines which declarations are declared
 * in this container, after which the other objects involved in the lookup 
 * can decide if the requested element is among these declarations.
 * 
 * @author Marko van Dooren
 */
public interface DeclarationContainer extends Element {
  
  /**
   * <p>Return the declarations the are defined in this declaration container.
   * The resulting collection contains the locally declared declarations and
   * those that this declaration container receives from other declaration
   * containers (e.g. through an inheritance relation).</p>
   *
   * <p>This method can be <b>very slow</b>. For example, when used on types/classes
   * for a language with syntactic overloading, this method is a disaster. 
   * Fortunately, all lookups are done using {@link #declarations(DeclarationSelector)}, 
   * which uses the selector to severely prune the collection of candidates. 
   * It is of course fine to use this method in the implementation of 
   * {@link #declarations(DeclarationSelector)} for declaration containers that 
   * have an efficient {@link #declarations()} method (most of them).</p>
   * 
   * @default The default implementation returns the {{@link #locallyDeclaredDeclarations()}.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post \result.containsAll(locallyDeclaredDeclarations());
   @*/
  public default List<? extends Declaration> declarations() throws LookupException {
    return locallyDeclaredDeclarations();
 }
  
  /**
   * @return the declarations that are defined locally in this declaration container.
   * @throws LookupException The list of declarations could not be computed
   * because of an error during lookup.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public default List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
  	return Lists.create();
  }
  
  /**
   * @return the declarations the are defined in this declaration container and
   * selected by the given declaration selector.
   * 
   * <p>Most implementations will directly invoke
   * selector.selection(declarations()), but in some cases, calculating the
   * collection of declarations is very expensive. In such cases, the selector
   * is typically pass along the chain of objects that contain the declarations
   * of this container. For example, a class will pass the selector to its super
   * classes instead of asking them for all declarations and then using the
   * selector. Applying the inheritance rules (such as overriding) to all class
   * members is very expensive, and useless for declarations that cannot be
   * selected anyway.</p>
   * 
   * @param selector
   *          The selector that determines which declarations must be returned.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post \result.equals(selector.selection(declarations()));
   @*/
  public default <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector)
      throws LookupException {
   return selector.selection(declarations());
}

  /**
   * {@inheritDoc}
   * 
   * @return a lookup context that searches for declarations in this container.
   * @throws LookupException
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public default LookupContext localContext() throws LookupException {
    return language().lookupFactory().createLocalLookupStrategy(this);
 }
  
  /**
   * {@inheritDoc}
   * 
   * @return the lexical lookup context created by the
   *         {@link LookupContextFactory#createLexicalLookupStrategy(LookupContext, Element)
   *         method of the {@link Language#lookupFactory()} of the
   *         {@link Element#language()} of this declaration container.
   */
  @Override
  public default LookupContext lookupContext(Element child) throws LookupException {
     return language().lookupFactory().createLexicalLookupStrategy(localContext(), this);
  }


}
