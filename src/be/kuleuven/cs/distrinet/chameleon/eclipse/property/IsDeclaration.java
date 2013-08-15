package be.kuleuven.cs.distrinet.chameleon.eclipse.property;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;

public class IsDeclaration extends ChameleonPropertyTester {

	@Override
	protected boolean testChameleonElement(Element element, String property, Object[] args,	Object expectedValue) {
		return element instanceof Declaration && property.equals("isDeclaration");
	}
}