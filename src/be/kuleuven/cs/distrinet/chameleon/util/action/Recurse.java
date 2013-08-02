package be.kuleuven.cs.distrinet.chameleon.util.action;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.rejuse.action.Action;

public class Recurse<T extends Element,E extends Exception> extends Walker<T,E> {
	
	public Recurse(Class<T> type, Action<? super T, ? extends E> action) {
		super(type);
		_action = action;
	}

	public Action<? super T, ? extends E> action() {
		return _action;
	}
	
	private Action<? super T, ? extends E> _action;

	protected void doPerform(T element) throws E {          
		for(Element child: element.children()){
			action().perform(child);
		}
	} 
}

