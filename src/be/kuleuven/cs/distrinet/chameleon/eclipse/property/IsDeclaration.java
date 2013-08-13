package be.kuleuven.cs.distrinet.chameleon.eclipse.property;

import java.util.List;

import org.eclipse.core.expressions.PropertyTester;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.eclipse.view.outline.ChameleonOutlineTree;

public class IsDeclaration extends PropertyTester {

	public IsDeclaration() {
		super();
	}
	
	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		try {
			ChameleonOutlineTree selection = (ChameleonOutlineTree) ((List)receiver).get(0);
			boolean result = property.equals("isDeclaration") && selection.getElement() instanceof Declaration;
			return result;
		} catch (Throwable e) {
			return false;
		}
		
	}

}