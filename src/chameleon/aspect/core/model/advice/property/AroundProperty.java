package chameleon.aspect.core.model.advice.property;

import org.rejuse.property.PropertyMutex;
import org.rejuse.property.PropertyUniverse;

import chameleon.aspect.core.model.advice.Advice;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.property.StaticChameleonProperty;

public class AroundProperty extends StaticChameleonProperty {
	
	public final static String ID = "advicetype.around";

	public AroundProperty(PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> mutex) {
		super(ID, universe, mutex, Advice.class);
	}
}
