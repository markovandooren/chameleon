package org.aikodi.chameleon.core.namespacedeclaration;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;

import be.kuleuven.cs.distrinet.rejuse.predicate.AbstractPredicate;

/**
 * A class for import statements.
 * 
 * @author Marko van Dooren
 */
public abstract class Import extends ElementImpl {

  /**
   * @return The elements that are directly imported by this import, if any.
   * @throws LookupException An exception occurred while looking for the
   *         imported declarations.
   */
  public abstract List<Declaration> directImports() throws LookupException;
  
  /**
   * @return The elements that are imported on demand by this import, if any.
   * @throws LookupException An exception occurred while looking for the
   *         imported declarations.
   */
  public abstract List<Declaration> demandImports() throws LookupException;

  /**
   * @param selector A selector that will be used for filtering the declarations.
   * @return The elements that are directly imported by this import, and
   *         that are selected by the given selector.
   * @throws LookupException An exception occurred while looking for the
   *         imported declarations.
   */
 /*@
   @ public behavior
   @
   @ pre selector != null;
   @
   @ post \result != null;
   @ post \result.stream().allMatch(d -> selector.selects(d));
   @*/
  public abstract <D extends Declaration> List<? extends SelectionResult> directImports(DeclarationSelector<D> selector) throws LookupException;
  
  /**
   * @param selector A selector that will be used for filtering the declarations.
   * @return The elements that are imported on demand by this import, and
   *         that are selected by the given selector.
   * @throws LookupException An exception occurred while looking for the
   *         imported declarations.
   */
 /*@
   @ public behavior
   @
   @ pre selector != null;
   @
   @ post \result != null;
   @ post \result.stream().allMatch(d -> selector.selects(d));
   @*/
  public abstract <D extends Declaration> List<? extends SelectionResult> demandImports(DeclarationSelector<D> selector) throws LookupException;
  
  /**
   * Check whether this import imports the same declarations as another import.
   * @param other The import for which must be checked if it imports the
   *              same declarations.
   * @return True if and only if the given import imports the same declarations
   *         as this import.
   * @throws LookupException An exception occurred while determining the
   *         declarations imported by this import or the given import.
   */
  public boolean importsSameAs(Import other) throws LookupException {
	  boolean result = sameDeclarations(demandImports(), other.demandImports());
	  result = result && sameDeclarations(directImports(), other.directImports());
	  return result;
  }

  private boolean sameDeclarations(List<Declaration> mine,
		  final List<Declaration> others) throws LookupException {
	  boolean result = new AbstractPredicate<Declaration, LookupException>() {

		  @Override
		  public boolean eval(final Declaration m) throws LookupException {
			  return new AbstractPredicate<Declaration, LookupException>() {

				  @Override
				  public boolean eval(Declaration o) throws LookupException {
					  boolean result = m.sameAs(o);
					  return result;
				  }

			  }.exists(others);
		  }
	  }.forAll(mine);
	  return result;
  }

  /**
   * {@inheritDoc}
   * 
   * Imports start looking in the default namespace.
   */
 /*@
   @ public behavior
   @
   @ post \result == view().namespace().targetContext();
   @*/
  @Override
  public LookupContext lexicalContext() throws LookupException {
  	return view().namespace().targetContext();
  }
  
}
