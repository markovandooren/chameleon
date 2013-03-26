package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.pattern;

public abstract class Dual extends DeclarationPattern {

	protected DeclarationPattern _first;
	protected DeclarationPattern _second;

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
