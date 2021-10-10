package org.aikodi.chameleon.ui.widget.tree;

import org.aikodi.rejuse.action.Nothing;
import org.aikodi.rejuse.data.tree.TreePredicate;

import java.util.Set;

public abstract class TristateTreePruner<X,Y> {
	
	public TristateTreePruner(TristateTreePruner<X,Y> next) {
		_next = next;
	}
	
	public TreePredicate<? super Y, Nothing> create(TreeNode<?, X> treeNode, Set<TreeNode<?,X>> checked, Set<TreeNode<?,X>> grayed) {
		return create(treeNode, checked, grayed, this);
	}

	private TristateTreePruner<X, Y> _next;

	protected TreePredicate<? super Y, Nothing> create(
			TreeNode<?, X> treeNode, 
			Set<TreeNode<?,X>> checked, 
			Set<TreeNode<?,X>> grayed,
			TristateTreePruner<X,Y> first) {
		
		TreePredicate<? super Y, Nothing> result = null;
		if(checked.contains(treeNode)) {
			result = checked(treeNode,checked,grayed, first);
		} else if(grayed.contains(treeNode)) {
			result = grayed(treeNode,checked,grayed, first);
		}	else {
			result = TreePredicate.False();
		}
		if(result == null) {
			if(_next != null) {
				result = _next.create(treeNode, checked, grayed, first);
			} else {
				result = TreePredicate.False();
			}
		}
		return result;
	}
	
	public TreePredicate<? super Y, Nothing> childrenDisjunction(TreeNode<?,X> node, Set<TreeNode<?,X>> checked,
			Set<TreeNode<?,X>> grayed,TristateTreePruner<X,Y>  first) {
		TreePredicate<? super Y, Nothing> result = TreePredicate.False();
		for(TreeNode<?,X> child: node.children()) {
			result = result.orTree((TreePredicate)create(child, checked, grayed,first));
		}
		return result;
	}


	protected abstract TreePredicate<? super Y, Nothing> grayed(TreeNode<?,X> node, Set<TreeNode<?,X>> checked, Set<TreeNode<?,X>> grayed,TristateTreePruner<X,Y>  first);

	protected abstract TreePredicate<? super Y, Nothing> checked(TreeNode<?,X> node, Set<TreeNode<?,X>> checked, Set<TreeNode<?,X>> grayed,TristateTreePruner<X,Y>  first);
}