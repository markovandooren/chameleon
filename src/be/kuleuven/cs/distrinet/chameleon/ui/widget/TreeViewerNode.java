package be.kuleuven.cs.distrinet.chameleon.ui.widget;

import java.util.List;

public abstract class TreeViewerNode<T> {

	public TreeViewerNode(TreeViewerNode<?> parent,T domainObject, String label) {
		_parent = parent;
		_domainObject = domainObject;
		_label = label;
	}
	
	public TreeViewerNode(T domainObject, String label) {
		this(null, domainObject,label);
	}
	
	public TreeViewerNode<?> parent() {
		return _parent;
	}
	
	private TreeViewerNode<?> _parent;
	
	public T domainObject() {
		return _domainObject;
	}
	
	private T _domainObject;
	
	public String label() {
		return _label;
	}
	
	private String _label;
	
	public abstract List<?> children();
	
}
