package org.aikodi.chameleon.util.action;

import org.aikodi.chameleon.core.element.Element;

import be.kuleuven.cs.distrinet.rejuse.tree.FunctionalTreeStructure;
import be.kuleuven.cs.distrinet.rejuse.tree.TreeStructure;

public class Recurse<T,E extends Exception> extends TreeAction<T,E> {
	
	public Recurse(TreeAction<T, ? extends E> action) {
		super(action.type());
		_action = action;
	}

	public TreeAction<T, ? extends E> walker() {
		return _action;
	}
	
	private TreeAction<T, ? extends E> _action;

	@Override
   protected <X extends T> void doPerform(TreeStructure<X> tree) throws E {
		for(X child: tree.children()){
			walker().enter(child);
			walker().perform(tree.tree(child));
			walker().exit(child);
		}
	} 

	@Override
	public <X extends T> void traverse(TreeStructure<X> tree) throws E {
	  X element = tree.node();
		for(X child: tree.children()){
			walker().enter(child);
      TreeStructure<? extends T> tree2 = tree.tree(child);
      walker().traverse(tree2);
			walker().exit(child);
		}
	} 
}

