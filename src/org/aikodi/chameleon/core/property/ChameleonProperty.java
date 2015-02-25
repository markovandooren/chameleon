package org.aikodi.chameleon.core.property;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.validation.Verification;

import be.kuleuven.cs.distrinet.rejuse.property.Property;

public interface ChameleonProperty extends Property<Element,ChameleonProperty> {

		public Verification verify(Element element);
		
		@Override
      public ChameleonProperty inverse();
		
		public void addValidElementType(Class<? extends Element> type);
}
