package be.kuleuven.cs.distrinet.chameleon.core.namespace;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.scope.LexicalScope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;

/**
 * @author Marko van Dooren
 */
public class NamespaceScope extends Scope {
  	
 /*@
   @ public behavior
   @
   @ pre pack != null;
   @
   @ post getPackage() == pack;
   @*/
  public NamespaceScope(Namespace ns) {
    _namespace = ns;
  }
  
	public boolean contains(Element element) throws LookupException {
		return  element.namespace().equals(getNamespace());
	}
  
 /*@
   @ also public behavior
   @
   @ post \result == (other instanceof NamespaceScope) && 
   @                 ((NamespaceScope)other).getPackage().equals(getPackage());
   @*/
  public boolean geRecursive(Scope other) throws LookupException {
    return (
             (other instanceof NamespaceScope) && 
             ((NamespaceScope)other).getNamespace().equals(getNamespace())
           )
           ||
           (
             (other instanceof LexicalScope) &&
             ((LexicalScope)other).element().namespace().equals(getNamespace())
           );
  }
  
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public Namespace getNamespace() {
    return _namespace;
  }

	private Namespace _namespace;

}
