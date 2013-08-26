package be.kuleuven.cs.distrinet.chameleon.analysis.dependency;

public class Dependency<S,C,T> {

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
