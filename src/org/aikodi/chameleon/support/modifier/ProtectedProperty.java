package org.aikodi.chameleon.support.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.namespace.NamespaceScope;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.core.scope.ScopeProperty;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.support.property.accessibility.HierarchyScope;
import org.aikodi.rejuse.property.PropertyMutex;
import org.aikodi.rejuse.property.PropertyUniverse;

public class ProtectedProperty extends ScopeProperty {
	
	public final static String ID = "accessibility.protected";
	
	public ProtectedProperty(PropertyMutex<ChameleonProperty> family) {
		super(ID, family);
	}

	public ProtectedProperty(String name, PropertyMutex<ChameleonProperty> family) {
		super(name, family);
	}

	@Override
   public Scope scope(Element element) throws ModelException {
		try {
			return new HierarchyScope((Type) element).union(new NamespaceScope(element.nearestAncestor(NamespaceDeclaration.class).namespace()));
		} catch (ClassCastException exc) {
			throw new ModelException("The given element is of the wrong type.");
		}
	}
}
