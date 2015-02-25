package org.aikodi.chameleon.support.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.namespace.NamespaceScope;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.core.scope.ScopeProperty;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.support.property.accessibility.HierarchyScope;

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

	@Override
   public Scope scope(Element element) throws ModelException {
		try {
			return new HierarchyScope((Type) element).union(new NamespaceScope(element.namespace()));
		} catch (ClassCastException exc) {
			throw new ModelException("The given element is of the wrong type.");
		}
	}
}
