package chameleon.core.scope;

import org.rejuse.property.PropertyMutex;
import org.rejuse.property.PropertyUniverse;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.property.StaticChameleonProperty;

public abstract class ScopeProperty extends StaticChameleonProperty {

	public ScopeProperty(String name, PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> family) {
		super(name, universe, family, Declaration.class);
	}

	public abstract Scope scope(Element element) throws MetamodelException;
}
