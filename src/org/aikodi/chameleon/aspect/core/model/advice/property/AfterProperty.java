package org.aikodi.chameleon.aspect.core.model.advice.property;

import org.aikodi.chameleon.aspect.core.model.advice.Advice;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.property.StaticChameleonProperty;
import org.aikodi.rejuse.property.PropertyMutex;
import org.aikodi.rejuse.property.PropertyUniverse;

public class AfterProperty extends StaticChameleonProperty {
	
	public final static String ID = "advicetype.after";

	public AfterProperty(PropertyMutex<ChameleonProperty> mutex) {
		super(ID, mutex, Advice.class);
	}
}
