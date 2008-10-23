package chameleon.core.accessibility;

import org.rejuse.property.PropertyMutex;
import org.rejuse.property.PropertyUniverse;
import org.rejuse.property.StaticProperty;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;

public abstract class AccessibilityProperty extends StaticProperty<Element> {

	public AccessibilityProperty(String name, PropertyUniverse<Element> universe, PropertyMutex<Element> family) {
		super(name, universe, family);
	}

	public abstract AccessibilityDomain accessibilityDomain(Element element) throws MetamodelException;
}
