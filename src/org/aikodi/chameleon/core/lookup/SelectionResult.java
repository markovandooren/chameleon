package org.aikodi.chameleon.core.lookup;

import org.aikodi.chameleon.core.declaration.Declaration;

/**
 * An object that can be the result of a lookup. This does not have to
 * be a model element, but can also be a container that keeps additional
 * information regarding the selection of a declaration, such as 
 * inferred type parameters.
 * 
 * @author Marko van Dooren
 */
public interface SelectionResult<D extends Declaration> {

  /**
   * <p>Return the declaration that is ultimately selected.</p>
   * <p>The default behavior is to simply return the selected declaration.
   * For language constructs such as methods with type parameters, which are
   * actually method templates, the template will be instantiated, and thus
   * a new method is created.</p>
   * 
   * @return The declaration that is ultimately selected if this selection
   * result is selected.
   * 
   * @throws LookupException
   */
	public D finalDeclaration() throws LookupException;
	
	/**
	 * <p>Update the selection result such that the result is part of the
	 * given declaration.</p>
	 * <p>The default behavior is to simple return the current object. For
	 * inheritance relations such as subobjects, the inherited declaration
	 * must be cloned and incorporated into the subobject.</p>
	 * 
	 * @param container The declaration that should contain the selection result.
	 * 
	 * @return A selection result that is "part of" the given container.
	 */
	public SelectionResult<D> updatedTo(Declaration container);
	
	/**
	 * Return the template from which the {@link #finalDeclaration()} is
	 * generated. Usually, this will be the final declaration itself. An
	 * example where this will not be the case is for methods that have
	 * type parameters. The overrides check does not take the actual
	 * type parameters into account, thus the templates are used for
	 * that check. 
	 * 
	 * @return The template from which the final declaration is generated.
	 */
	public Declaration template();
}
