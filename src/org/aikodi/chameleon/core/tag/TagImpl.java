package org.aikodi.chameleon.core.tag;

import org.aikodi.chameleon.core.element.Element;

public class TagImpl implements Metadata {

	private Element _element;

	@Override
	public final Element element() {
		return _element;
	}

	private String name() {
		return _name;
	}

	private String _name;

	@Override
	public void disconnect() {
		if (_element != null) {
			_element.removeMetadata(name());
		}
	}

	@Override
	public void setElement(Element element, String name) {
		if (element != _element) {
			// Set new pointer, backup old for removal.
			_element = element;
			_name = name;
			// Remove from current element.
			if ((_element != null) && (_element.metadata(name) == this)) {
				_element.removeMetadata(name);
			}
			// Add to new element.
			if ((_element != null) && (_element.metadata(name) != this)) {
				_element.setMetadata(this, name);
			}
		}
	}
}
