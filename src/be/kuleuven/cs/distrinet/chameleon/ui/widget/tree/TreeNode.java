package be.kuleuven.cs.distrinet.chameleon.ui.widget.tree;

import java.util.List;

import com.google.common.collect.ImmutableList;

public abstract class TreeNode<T extends G,G> {

	public TreeNode(TreeNode<?,G> parent,T domainObject, String label) {
		_parent = parent;
		_domainObject = domainObject;
		_label = label;
	}
	
	public TreeNode(T domainObject, String label) {
		this(null, domainObject,label);
	}
	
	public TreeNode<?,G> parent() {
		return _parent;
	}
	
	private TreeNode<?,G> _parent;
	
	public T domainObject() {
		return _domainObject;
	}
	
	private T _domainObject;
	
	public String label() {
		return _label;
	}
	
	private String _label;
	
	public final List<? extends TreeNode<?,G>> children() {
		if(_children == null) {
			_children = ImmutableList.copyOf(createChildren());
		}
		return _children;
	}
	
	protected abstract List<? extends TreeNode<?,G>> createChildren();
	
	private ImmutableList<? extends TreeNode<?,G>> _children;
	
}
