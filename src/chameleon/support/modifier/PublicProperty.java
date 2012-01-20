package chameleon.support.modifier;

import org.rejuse.property.PropertyMutex;
import org.rejuse.property.PropertyUniverse;

import chameleon.core.element.Element;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;
import chameleon.core.scope.UniversalScope;

public class PublicProperty extends ScopeProperty {
	
	public final static String ID = "accessibility.public";
	
	public PublicProperty(PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> family) {
		super(ID, universe, family);
	}

	public PublicProperty(String name, PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> family) {
		super(name, universe, family);
	}

	public Scope scope(Element element) {
			return new UniversalScope();
	}
}