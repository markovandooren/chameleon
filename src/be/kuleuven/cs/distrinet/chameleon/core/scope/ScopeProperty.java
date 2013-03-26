package be.kuleuven.cs.distrinet.chameleon.core.scope;

import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyUniverse;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.property.StaticChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;

public abstract class ScopeProperty extends StaticChameleonProperty {

	public ScopeProperty(String name, PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> family) {
		super(name, universe, family, Declaration.class);
	}

	public abstract Scope scope(Element element) throws ModelException;
}
