package be.kuleuven.cs.distrinet.chameleon.util.action;

public class TopDown<E extends Exception> extends Sequence<E> {

	public TopDown(Walker<? extends E> walker) {
		super(walker, null);
		setSecond(new Recurse<>(this));
	}

  
	
}
