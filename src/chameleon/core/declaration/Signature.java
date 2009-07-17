package chameleon.core.declaration;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespacepart.NamespaceElementImpl;

/**
 * A signature is a means of identifying a declaration that can be cross-referenced. It contains
 * only the information required for identification. The return type of a method, for example,
 * is not part of its signature.
 * 
 * @author Marko van Dooren
 */
public abstract class Signature<E extends Signature, P extends Element> extends NamespaceElementImpl<E,P> {

  public abstract E clone();
  
  /**
   * Equals cannot throw a checked exception, so we introduce sameAs.
   */
  public abstract boolean sameAs(Signature other) throws LookupException;
  
}
