package chameleon.core.property;

import be.kuleuven.cs.distrinet.rejuse.property.Property;
import chameleon.core.element.Element;
import chameleon.core.validation.VerificationResult;

public interface ChameleonProperty extends Property<Element,ChameleonProperty> {

		public VerificationResult verify(Element element);
		
		public ChameleonProperty inverse();
		
		public void addValidElementType(Class<? extends Element> type);
}
