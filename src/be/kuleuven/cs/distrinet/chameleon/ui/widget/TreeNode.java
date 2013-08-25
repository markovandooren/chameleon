package be.kuleuven.cs.distrinet.chameleon.ui.widget;

import java.util.List;

import com.google.common.collect.ImmutableList;

public abstract class TreeNode<T> {

	public TreeNode(TreeNode<?> parent,T domainObject, String label) {
		_parent = parent;
		_domainObject = domainObject;
		_label = label;
	}
	
	public TreeNode(T domainObject, String label) {
		this(null, domainObject,label);
	}
	
	public TreeNode<?> parent() {
		return _parent;
	}
	
	private TreeNode<?> _parent;
	
	public T domainObject() {
		return _domainObject;
	}
	
	private T _domainObject;
	
	public String label() {
		return _label;
	}
	
	private String _label;
	
	public final List<? extends TreeNode<?>> children() {
		if(_children == null) {
			_children = ImmutableList.copyOf(createChildren());
		}
		return _children;
	}
	
	protected abstract List<? extends TreeNode<?>> createChildren();
	
	private ImmutableList<? extends TreeNode<?>> _children;
	
}
