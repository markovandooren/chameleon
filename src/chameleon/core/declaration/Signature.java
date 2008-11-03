package chameleon.core.declaration;

import java.util.Iterator;
import java.util.List;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;
import chameleon.core.namespacepart.NamespacePartElementImpl;

/**
 * A signature is a means of identifying an element that can be crossreferenced. It contains
 * only the information required for identification.
 * 
 * 
 * @author Marko van Dooren
 */
public abstract class Signature<E extends Signature, P extends Element> extends NamespacePartElementImpl<E,P> {
  
  public abstract E clone();
  
  /**
   * Equals cannot throw a checked exception, so we introduce sameAs.
   * @param other
   * @return
   * @throws MetamodelException
   */
  public abstract boolean sameAs(Signature other) throws MetamodelException;
  
  public abstract List<String> identifiers();
  
  public int nbIdentifiers() {
  	return identifiers().size();
  }
  
  /**
   * The first valid index is 1.
   * @param index
   * @return
   */
  public String identifierAt(int index) {
  	return identifiers().get(index - 1);
  }
  
  public boolean sameIdentifiersAs(Signature other) {
  	boolean result = false;
  	if(other != null) {
  		List<String> first = identifiers();
  		List<String> second = other.identifiers();
  	  Iterator<String> iter1 = first.iterator();
  	  Iterator<String> iter2 = second.iterator();
  	  result = (first.size() == second.size());
  	  while(result && iter1.hasNext()) {
  	  	result = iter1.next().equals(iter2.next());
  	  }
  	}
  	return result;
  }
}
