package be.kuleuven.cs.distrinet.chameleon.ui.widget.tree;

import java.util.Set;

import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.False;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public abstract class TristateTreePruner<X,Y> {
	
	public TristateTreePruner(TristateTreePruner<X,Y> next) {
		_next = next;
	}
	
	public UniversalPredicate<? super Y, Nothing> create(TreeNode<?, X> treeNode, Set<TreeNode<?,X>> checked, Set<TreeNode<?,X>> grayed) {
		return create(treeNode, checked, grayed, this);
	}

	private TristateTreePruner<X, Y> _next;

	protected UniversalPredicate<? super Y, Nothing> create(
			TreeNode<?, X> treeNode, 
			Set<TreeNode<?,X>> checked, 
			Set<TreeNode<?,X>> grayed,
			TristateTreePruner<X,Y> first) {
		
		UniversalPredicate<? super Y, Nothing> result = null;
		if(checked.contains(treeNode)) {
			result = checked(treeNode,checked,grayed, first);
		} else if(grayed.contains(treeNode)) {
			result = grayed(treeNode,checked,grayed, first);
		}	else {
			result = new False();
		}
		if(result == null) {
			if(_next != null) {
				result = _next.create(treeNode, checked, grayed, first);
			} else {
				result = new False();
			}
		}
		return result;
	}
	
	public UniversalPredicate<? super Y, Nothing> childrenDisjunction(TreeNode<?,X> node, Set<TreeNode<?,X>> checked,
			Set<TreeNode<?,X>> grayed,TristateTreePruner<X,Y>  first) {
		UniversalPredicate<? super Y, Nothing> result = new False();
		for(TreeNode<?,X> child: node.children()) {
			result = result.or((UniversalPredicate)create(child, checked, grayed,first));
		}
		return result;
	}


	protected abstract UniversalPredicate<? super Y, Nothing> grayed(TreeNode<?,X> node, Set<TreeNode<?,X>> checked, Set<TreeNode<?,X>> grayed,TristateTreePruner<X,Y>  first);

	protected abstract UniversalPredicate<? super Y, Nothing> checked(TreeNode<?,X> node, Set<TreeNode<?,X>> checked, Set<TreeNode<?,X>> grayed,TristateTreePruner<X,Y>  first);
}