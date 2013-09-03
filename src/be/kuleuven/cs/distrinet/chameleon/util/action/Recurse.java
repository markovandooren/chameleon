package be.kuleuven.cs.distrinet.chameleon.util.action;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.tree.TreeStructure;

public class Recurse<T extends Element,E extends Exception> extends TreeAction<T,E> {
	
	public Recurse(TreeAction<T, ? extends E> action) {
		super(action.type());
		_action = action;
	}

	public TreeAction<T, ? extends E> walker() {
		return _action;
	}
	
	private TreeAction<T, ? extends E> _action;

	protected void doPerform(T element) throws E {          
		for(Element child: element.children()){
			walker().enter(child);
			walker().perform(child);
			walker().exit(child);
		}
	} 

	@Override
	public void traverse(T element, TreeStructure<? extends T> tree) throws E {          
		for(T child: ((TreeStructure<T>)tree).children(element)){
			walker().enter(child);
			walker().traverse(child,tree);
			walker().exit(child);
		}
	} 
}

