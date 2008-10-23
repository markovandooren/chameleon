package chameleon.core.namespace;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;

public class NamespaceSignature extends Signature<NamespaceSignature, Namespace>{

	public NamespaceSignature(String name) {
		setName(name);
	}
	
	private String _name;
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	@Override
	public NamespaceSignature clone() {
		return new NamespaceSignature(getName());
	}

	public List<? extends Element> getChildren() {
		return new ArrayList();
	}

	@Override
	public boolean sameAs(Object other) throws MetamodelException {
		boolean result = false;
		if(other instanceof NamespaceSignature) {
			NamespaceSignature sig = (NamespaceSignature)other;
			result = getName().equals(sig.getName());
		}
		return result;
	}

}
