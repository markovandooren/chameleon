package chameleon.core.scope;

import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyUniverse;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.property.StaticChameleonProperty;
import chameleon.exception.ModelException;

public abstract class ScopeProperty extends StaticChameleonProperty {

	public ScopeProperty(String name, PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> family) {
		super(name, universe, family, Declaration.class);
	}

	public abstract Scope scope(Element element) throws ModelException;
}
