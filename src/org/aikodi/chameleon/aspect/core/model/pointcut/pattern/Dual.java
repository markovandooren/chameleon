package org.aikodi.chameleon.aspect.core.model.pointcut.pattern;

public abstract class Dual extends DeclarationPattern {

	private DeclarationPattern _first;
	private DeclarationPattern _second;

	public Dual(DeclarationPattern first, DeclarationPattern second) {
		_first = first;
		_second = second;
	}

	public DeclarationPattern first() {
		return _first;
	}

	public DeclarationPattern second() {
		return _second;
	}

}
