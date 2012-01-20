package chameleon.support.property.accessibility;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.scope.LexicalScope;
import chameleon.core.scope.Scope;
import chameleon.oo.type.Type;

/**
 * @author Marko van Dooren
 */
public class HierarchyScope extends Scope {
  
 /*@
   @ public behavior
   @
   @ pre type != null;
   @
   @ post getType() == type;
   @*/
  public HierarchyScope(Type type) {
    _type = type;
  }
  
  public boolean contains(Element element) throws LookupException {
  	Type type = (Type) element.nearestAncestor(Type.class);
  	if(type != null) {
		  return type.subTypeOf(getType());
  	} else {
  		return false;
  	}
  }

 /*@
   @ also public behavior
   @
   @ post \result == ((other instanceof HierarchyScope) && 
   @                  ((HierarchyScope)other).getType().assignableTo(getType())) ||
   @                 ((other instanceof LexicalScope) && 
   @		 	            ((LexicalScope)other).element().nearestAncestor(Type.class).assignableTo(getType()));
   @*/
  public boolean geRecursive(Scope other) throws LookupException {
    return (
    		    (other instanceof HierarchyScope) && 
            ((HierarchyScope)other).getType().assignableTo(getType())
           ) ||
           (
            (other instanceof LexicalScope) && 
    		 	  ((LexicalScope)other).element().nearestAncestor(Type.class).assignableTo(getType())
           )
             ;
  }
  
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public Type getType() throws LookupException {
    return _type;
  }

	private Type _type;

}
