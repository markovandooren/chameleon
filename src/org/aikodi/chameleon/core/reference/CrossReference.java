package org.aikodi.chameleon.core.reference;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;

/**
 * <p>This interface represents the concept of a cross-reference in the model. A
 * construct is a cross-reference if it points to another element in the model
 * instead of containing it as a descendant or having it as an ancestor. For
 * example, when a variable is access in source code, the variable is referenced
 * by writing the name of that variable. It is the job of the lookup mechanism
 * to find out which variable that is.</p>
 * 
 * <p>Cross-references are <b>never</b> modeled with direct object references.
 * Instead a cross-reference offers the {@link #getElement()} method to obtain
 * the referenced object. Internally, a cross-reference may store direct
 * references in its cache, but these are cleared when the model is modified.
 * The reason for this choice is twofold. First, when a part of the model is
 * modified, it is very hard to find out which cross-references points to a
 * different element (possibly none) after the change. In most cases, every
 * cross-reference in the entire model could be affected. But most importantly,
 * this way of modeling corresponds directly to the semantics of the code. The
 * variable name "a" in the source code is not tied to a particular variable
 * with name "a". Instead, its semantics is that it refers to whatever variable
 * with name "a" that it in scope in the current state of the model. If we
 * remove the variable that it currently refers to, it will simply refer to
 * another variable with name "a" in scope (if such a variable exists).</p>
 * 
 * <p>All logical structures are implemented in terms of cross-references. For
 * example, a subclass relation will contain a cross-reference that points to
 * the superclass.</p>
 * 
 * <p>Note that the result of {@link #getElement()} is not necessarily a part of
 * the lexical model. For example a C# property also defines methods. These
 * references will be found by the lookup mechanism, but they are not part of
 * the base C# model. In case of such generated elements, the
 * {@link #getDeclarator()} method will return the element that generated the
 * referenced element.</p>
 * 
 * @author Marko van Dooren
 * @author Tim Vermeiren
 * 
 *         <D> The type of the declaration that is referenced by this 
 *         cross-reference.
 */
public interface CrossReference<D extends Declaration> extends Element, CrossReferenceTarget {

  /**
   * Return the element referenced by this cross-reference. See
   * {@link chameleon.core.lookup.LookupContext} to learn how you can make sure
   * that you cross-reference can reuse as much functionality from the Chameleon
   * framework as possible.
   * 
   * @throws LookupException
   *           A LookupException is thrown if the model cannot guarantee to find
   *           a correct result. This happens when at some point during the
   *           lookup, a required element cannot be found, or if there are
   *           multiple equivalent candidates.
   */
  public D getElement() throws LookupException;

  /**
   * <p>
   * Return the element that declared the element that is returned by
   * getElement(). In most cases, both methods return the same element, but for
   * variable, getElement() returns the variable, whereas getDeclarator() may
   * return a variable declarator. The same goes for type names that refer to
   * type a parameter. In this case getElement() returns the type, and
   * getDeclarator() returns the type parameter.
   * </p>
   * 
   * <p> This method is used e.g. in IDEs to jump to the definition of an element 
   * in the source code.</p>
   * 
   * @return
   * @throws LookupException
   */
  public default Declaration getDeclarator() throws LookupException {
    return getElement().declarator();
  }

  /**
   * By default returns the target context of the referenced element.
   * 
   * @{inheritDoc}
   */
  @Override
  public default LookupContext targetContext() throws LookupException {
    return getElement().targetContext();
  }

  /**
   * @return The lowest upper bound of the type of the element that this cross-reference
   *         can point to. This is not necessarily the exact class of the referenced element.
   */
  public Class<D> referencedType();
}
