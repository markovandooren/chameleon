package org.aikodi.chameleon.analysis.dependency;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;

public class Dependency<S extends Element,C,T extends Declaration> {

	public Dependency(S source, C crossReference, T target) {
		super();
		this._source = source;
		this._crossReference = crossReference;
		this._target = target;
	}

	private S _source;
	
	public S source() {
		return _source;
	}

	private C _crossReference;
	
	public C crossReference() {
		return _crossReference;
	}

	public T target() {
		return _target;
	}

	private T _target;
}
