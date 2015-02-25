package org.aikodi.chameleon.core.element;

import org.aikodi.chameleon.exception.ChameleonProgrammerException;

public class CloneException extends ChameleonProgrammerException {

	public CloneException() {
	}

	public CloneException(Exception e) {
		super(e);
	}

	public CloneException(String msg, Exception e) {
		super(msg,e);
	}

	public CloneException(String msg) {
		super(msg);
	}

}
