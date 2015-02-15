package be.kuleuven.cs.distrinet.chameleon.support.modifier;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.ScopeProperty;
import be.kuleuven.cs.distrinet.chameleon.core.scope.UniversalScope;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyUniverse;

public class PublicProperty extends ScopeProperty {
	
	public final static String ID = "accessibility.public";
	
	public PublicProperty(PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> family) {
		super(ID, universe, family);
	}

	public PublicProperty(String name, PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> family) {
		super(name, universe, family);
	}

	@Override
   public Scope scope(Element element) {
			return new UniversalScope();
	}
}
