package org.aikodi.chameleon.core.namespace;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.scope.LexicalScope;
import org.aikodi.chameleon.core.scope.Scope;

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
  
	@Override
   public boolean contains(Element element) throws LookupException {
		return  element.namespace().equals(getNamespace());
	}
  
 /*@
   @ also public behavior
   @
   @ post \result == (other instanceof NamespaceScope) && 
   @                 ((NamespaceScope)other).getPackage().equals(getPackage());
   @*/
  @Override
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
