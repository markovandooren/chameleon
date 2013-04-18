package be.kuleuven.cs.distrinet.chameleon.support.modifier;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.NamespaceScope;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.ScopeProperty;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.support.property.accessibility.HierarchyScope;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyUniverse;

public class ProtectedProperty extends ScopeProperty {
	
	public final static String ID = "accessibility.protected";
	
	public ProtectedProperty(PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> family) {
		super(ID, universe, family);
	}

	public ProtectedProperty(String name, PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> family) {
		super(name, universe, family);
	}

	public Scope scope(Element element) throws ModelException {
		try {
			return new HierarchyScope((Type) element).union(new NamespaceScope(element.namespace()));
		} catch (ClassCastException exc) {
			throw new ModelException("The given element is of the wrong type.");
		}
	}
}
