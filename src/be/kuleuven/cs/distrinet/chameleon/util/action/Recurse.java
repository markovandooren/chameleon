package be.kuleuven.cs.distrinet.chameleon.util.action;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.rejuse.action.Action;

public class Recurse<T extends Element,E extends Exception> extends Walker<T,E> {
	
	public Recurse(Walker<T, ? extends E> action) {
		super(action.type());
		_action = action;
	}

	public Walker<T, ? extends E> walker() {
		return _action;
	}
	
	private Walker<T, ? extends E> _action;

	protected void doPerform(T element) throws E {          
		for(Element child: element.children()){
			walker().enter(child);
			walker().perform(child);
			walker().exit(child);
		}
	} 
}

