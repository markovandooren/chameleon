package be.kuleuven.cs.distrinet.chameleon.util.action;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.rejuse.action.Action;

public class Recurse<E extends Exception> extends Walker<E> {
	
	public Recurse(Action<? super Element, ? extends E> action) {
		_action = action;
	}

	public Action<? super Element, ? extends E> action() {
		return _action;
	}
	
	private Action<? super Element, ? extends E> _action;

	protected void doPerform(Element element) throws E {          
		for(Element child: element.children()){
			action().perform(child);
		}
	} 
}

