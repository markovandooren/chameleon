package org.aikodi.chameleon.aspect.oo.model.advice.property;

import org.aikodi.chameleon.aspect.core.model.advice.Advice;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.property.StaticChameleonProperty;
import org.aikodi.rejuse.property.PropertyMutex;

public class ThrowingProperty extends StaticChameleonProperty {
	
	public final static String ID = "advicetype.throwing";

	public ThrowingProperty(PropertyMutex<ChameleonProperty> mutex) {
		super(ID, mutex, Advice.class);
	}
}
