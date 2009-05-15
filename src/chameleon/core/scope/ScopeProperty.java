package chameleon.core.scope;

import org.rejuse.property.PropertyMutex;
import org.rejuse.property.PropertyUniverse;
import org.rejuse.property.StaticProperty;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;

public abstract class ScopeProperty extends StaticProperty<Element> {

	public ScopeProperty(String name, PropertyUniverse<Element> universe, PropertyMutex<Element> family) {
		super(name, universe, family);
	}

	public abstract Scope scope(Element element) throws MetamodelException;
}
