package org.aikodi.chameleon.input;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.plugin.ViewProcessorImpl;

public class DefaultInputProcessor extends ViewProcessorImpl implements InputProcessor {

	@Override
	public void setLocation(Element element, int offset, int length, Document compilationUnit, String tagType) {
	}

	@Override
	public void removeLocations(Element element) {
//		element.removeMetadata(key);
	}

	@Override
	public void markParseError(int offset, int length, String message, Element element) {
	}

	@Override
	public ViewProcessorImpl clone() {
		return new DefaultInputProcessor();
	}

}
