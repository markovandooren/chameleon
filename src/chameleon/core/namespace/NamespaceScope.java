package chameleon.core.namespace;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;
import chameleon.core.scope.LexicalScope;
import chameleon.core.scope.Scope;

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
  
	public boolean contains(Element element) {
		return (element instanceof NamespaceElement) && (((NamespaceElement)element).getNamespace().equals(getNamespace()));
	}
  
 /*@
   @ also public behavior
   @
   @ post \result == (other instanceof NamespaceScope) && 
   @                 ((NamespaceScope)other).getPackage().equals(getPackage());
   @*/
  public boolean geRecursive(Scope other) throws MetamodelException {
    return (
             (other instanceof NamespaceScope) && 
             ((NamespaceScope)other).getNamespace().equals(getNamespace())
           )
           ||
           (
             (other instanceof LexicalScope) &&
             ((LexicalScope)other).element().getNamespace().equals(getNamespace())
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
