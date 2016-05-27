package org.aikodi.chameleon.support.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.core.scope.ScopeProperty;
import org.aikodi.chameleon.core.scope.UniversalScope;

import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyUniverse;

public class PublicProperty extends ScopeProperty {
	
	public final static String ID = "accessibility.public";
	
	public PublicProperty(PropertyMutex<ChameleonProperty> family) {
		super(ID, family);
	}

	public PublicProperty(String name, PropertyMutex<ChameleonProperty> family) {
		super(name, family);
	}

	@Override
   public Scope scope(Element element) {
			return new UniversalScope();
	}
}
