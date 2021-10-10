package org.aikodi.chameleon.aspect.core.model.advice.property;

import org.aikodi.chameleon.aspect.core.model.advice.Advice;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.property.StaticChameleonProperty;
import org.aikodi.rejuse.property.PropertyMutex;

public class AroundProperty extends StaticChameleonProperty {
	
	public final static String ID = "advicetype.around";

	public AroundProperty(PropertyMutex<ChameleonProperty> mutex) {
		super(ID, mutex, Advice.class);
	}
}
