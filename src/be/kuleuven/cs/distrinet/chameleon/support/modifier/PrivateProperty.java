package be.kuleuven.cs.distrinet.chameleon.support.modifier;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.scope.LexicalScope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.ScopeProperty;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyUniverse;

public class PrivateProperty extends ScopeProperty {
	
	public final static String ID = "accessibility.private";
	
	public PrivateProperty(PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> family) {
		this(ID, universe, family);
  }
	public PrivateProperty(String name, PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> family) {
		super(name, universe, family);
	}

	public Scope scope(Element element) throws LookupException {
		try {
			return new LexicalScope(((Type)element.farthestAncestor(Type.class)));
		} catch (ClassCastException exc) {
			throw new LookupException("Private property does not support elements that are no TypeDescendant.");
		}
	}

}
