package chameleon.core.declaration;

import chameleon.core.context.LookupException;
import chameleon.core.element.Element;
import chameleon.core.namespacepart.NamespacePartElementImpl;

/**
 * A signature is a means of identifying a declaration that can be cross-referenced. It contains
 * only the information required for identification.
 * 
 * 
 * @author Marko van Dooren
 */
public abstract class Signature<E extends Signature, P extends Element> extends NamespacePartElementImpl<E,P> {

  public abstract E clone();
  
  /**
   * Equals cannot throw a checked exception, so we introduce sameAs.
   */
  public abstract boolean sameAs(Signature other) throws LookupException;
  
//  /**
//   * Return the list of identifiers of this signature.
//   */
// /*@
//   @ public behavior
//   @
//   @ post \result != null;
//   @*/
//  public final List<String> identifiers() {return null;}
//
//  /**
//   * Return the number of identifiers in this signature.
//   */
// /*@
//   @ public behavior
//   @
//   @ post \result == identifiers().size();
//   @*/
//  public final int nbIdentifiers() {
//  	return identifiers().size();
//  }
//  
//  /**
//   * Return the indentifier with the given index. The first valid index is 1.
//   */
// /*@
//   @ public behavior
//   @
//   @ post \result == identifiers().get(index - 1);
//   @*/
//  public final String identifierAt(int index) {
//  	return identifiers().get(index - 1);
//  }
//  
//  /**
//   * Check whether this signature has the same list of identifiers as another signature.
//   */
// /*@
//   @ post \result == (other != null) &&
//   @                 (other.nbIdentifiers() == nbIdentifiers()) &&
//   @                 (\forall i; i >= 1 && i <= nbIdentifiers; identifierAt(i).equals(other.identifierAt(i)));
//   @*/
//  public final boolean sameIdentifiersAs(Signature other) {
//  	boolean result = false;
//  	if(other != null) {
//  		List<String> first = identifiers();
//  		List<String> second = other.identifiers();
//  	  Iterator<String> iter1 = first.iterator();
//  	  Iterator<String> iter2 = second.iterator();
//  	  result = (first.size() == second.size());
//  	  while(result && iter1.hasNext()) {
//  	  	result = iter1.next().equals(iter2.next());
//  	  }
//  	}
//  	return result;
//  }
}
