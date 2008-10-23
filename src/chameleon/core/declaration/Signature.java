package chameleon.core.declaration;

import chameleon.core.MetamodelException;
import chameleon.core.namespacepart.NamespacePartElementImpl;

/**
 * A signature is a means of identifying an element that can be crossreferenced. It contains
 * only the information required for identification.
 * 
 * 
 * @author Marko van Dooren
 */
public abstract class Signature<E extends Signature, P extends Declaration> extends NamespacePartElementImpl<E,P> {
  
  public abstract E clone();
  
  /**
   * Equals cannot throw a checked exception, so we introduce sameAs.
   * @param other
   * @return
   * @throws MetamodelException
   */
  public abstract boolean sameAs(Object other) throws MetamodelException;
}
