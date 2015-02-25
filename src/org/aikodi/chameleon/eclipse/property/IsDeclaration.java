package org.aikodi.chameleon.eclipse.property;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;

public class IsDeclaration extends ChameleonPropertyTester {

	@Override
	protected boolean testChameleonElement(Element element, String property, Object[] args,	Object expectedValue) {
		return element instanceof Declaration && property.equals("isDeclaration");
	}
}