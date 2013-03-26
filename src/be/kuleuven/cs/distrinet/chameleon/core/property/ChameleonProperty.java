package be.kuleuven.cs.distrinet.chameleon.core.property;

import be.kuleuven.cs.distrinet.rejuse.property.Property;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;

public interface ChameleonProperty extends Property<Element,ChameleonProperty> {

		public VerificationResult verify(Element element);
		
		public ChameleonProperty inverse();
		
		public void addValidElementType(Class<? extends Element> type);
}
