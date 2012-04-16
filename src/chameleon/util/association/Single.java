package chameleon.util.association;

import org.rejuse.association.Association;
import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;

public class Single<T extends Element> extends SingleAssociation<Element, T> {

	public Single(Element element) {
		super(element);
	}

	public Single(Element element, Association<? extends T, ? super Element> other) {
		super(element, other);
	}
	
	public Single(Element element, boolean mandatory) {
		this(element);
		_mandatory = mandatory;
	}

	public Single(Element element, Association<? extends T, ? super Element> other, boolean mandatory) {
		this(element, other);
		_mandatory = mandatory;
	}
	
	private boolean _mandatory = false;
	
	public boolean mandatory() {
		return _mandatory;
	}
}
