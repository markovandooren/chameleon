package be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.treeview;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;

public abstract class PrefixIconDecorator implements NameBasedIconDecorator {

	@Override
   public String decorate(Element element, String baseIconName) throws ModelException {
		String result = baseIconName;
		if(appliesTo(element)) {
			result = prefix(element)+baseIconName;
		}
		return result;
	}

	public abstract boolean appliesTo(Element element) throws ModelException;
	
	public abstract String prefix(Element element) throws ModelException;
	
}
