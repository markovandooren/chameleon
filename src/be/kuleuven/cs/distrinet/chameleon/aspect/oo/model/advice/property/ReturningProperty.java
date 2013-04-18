package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.property;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.advice.Advice;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.property.StaticChameleonProperty;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyUniverse;

public class ReturningProperty extends StaticChameleonProperty {
	
	public final static String ID = "advicetype.returning";

	public ReturningProperty(PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> mutex) {
		super(ID, universe, mutex, Advice.class);
	}
}
