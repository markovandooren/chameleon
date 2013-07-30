package be.kuleuven.cs.distrinet.chameleon.util.action;


public class DepthFirst<E extends Exception> extends Sequence<E> {

	public DepthFirst(Walker<? extends E> walker) {
		super(null, walker);
		setFirst(new Recurse<>(this));
	}
}
