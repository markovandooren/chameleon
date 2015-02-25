package org.aikodi.chameleon.aspect.core.model.pointcut.pattern;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;

public class TypePattern extends DeclarationPattern {

	/**
	 * Initialize a new type pattern with the given type
	 * @param kind
	 */
 /*@
   @ pre type != null;
   @
   @ post type() == type;
   @*/
	public TypePattern(Class<? extends Element> type) {
		_type = type;
	}
	
 /*@
	 @ public behavior
	 @
	 @ post \result == type().isInstance(declaration); 
	 @*/
	@Override
	public boolean eval(Declaration declaration) throws LookupException {
		return type().isInstance(declaration);
	}

	/**
	 * Return the type of the elements that match this pattern.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public Class<? extends Element> type() {
		return _type;
	}
	
	private Class<? extends Element> _type;
	
	@Override
	public DeclarationPattern clone() {
		return new TypePattern(type());
	}

}
