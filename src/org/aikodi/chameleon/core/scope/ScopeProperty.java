package org.aikodi.chameleon.core.scope;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.property.StaticChameleonProperty;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.rejuse.property.PropertyMutex;
import org.aikodi.rejuse.property.PropertyUniverse;

public abstract class ScopeProperty extends StaticChameleonProperty {

	public ScopeProperty(String name, PropertyMutex<ChameleonProperty> family) {
		super(name, family, Declaration.class);
	}

	public abstract Scope scope(Element element) throws ModelException;
}
