package org.aikodi.chameleon.support.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.scope.LexicalScope;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.core.scope.ScopeProperty;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.rejuse.property.PropertyMutex;

public class PrivateProperty extends ScopeProperty {
	
	public final static String ID = "accessibility.private";
	
	public PrivateProperty(PropertyMutex<ChameleonProperty> family) {
		this(ID, family);
  }
	public PrivateProperty(String name, PropertyMutex<ChameleonProperty> family) {
		super(name, family);
	}

	@Override
   public Scope scope(Element element) throws LookupException {
		try {
			return new LexicalScope((element.lexical().farthestAncestor(Type.class)));
		} catch (ClassCastException exc) {
			throw new LookupException("Private property does not support elements that are no TypeDescendant.");
		}
	}

}
