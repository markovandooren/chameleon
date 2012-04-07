package chameleon.util.association;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.element.Element;

public class Multi<T extends Element> extends OrderedMultiAssociation<Element, T> {

	public Multi(Element element) {
		super(element);
	}
	
	public Multi(Element element, int min, int max) {
		this(element);
		_min=min;
		_max=max;
	}
	
	public int min() {
		return _min;
	}
	
	public int max() {
		return _max;
	}
	
	private int _min;
	private int _max;

}
