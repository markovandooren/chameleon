package chameleon.core.scope;

import org.rejuse.property.PropertyMutex;
import org.rejuse.property.PropertyUniverse;
import org.rejuse.property.StaticProperty;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;
import chameleon.core.property.ChameleonProperty;

public abstract class ScopeProperty extends StaticProperty<Element,ChameleonProperty> implements ChameleonProperty {

	public ScopeProperty(String name, PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> family) {
		super(name, universe, family);
	}

	public abstract Scope scope(Element element) throws MetamodelException;
}
