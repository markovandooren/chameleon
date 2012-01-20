package chameleon.support.modifier;

import org.rejuse.property.PropertyMutex;
import org.rejuse.property.PropertyUniverse;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElement;
import chameleon.core.namespace.NamespaceScope;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;
import chameleon.exception.ModelException;
import chameleon.oo.type.Type;
import chameleon.support.property.accessibility.HierarchyScope;

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
			return new HierarchyScope((Type) element).union(new NamespaceScope(((NamespaceElement) element).getNamespace()));
		} catch (ClassCastException exc) {
			throw new ModelException("The given element is of the wrong type.");
		}
	}
}