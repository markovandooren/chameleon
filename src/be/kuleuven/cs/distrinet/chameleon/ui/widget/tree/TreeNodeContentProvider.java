package be.kuleuven.cs.distrinet.chameleon.ui.widget.tree;

import java.util.List;

public abstract class TreeNodeContentProvider<G> extends TreeContentProvider<G> {

	@SuppressWarnings("unchecked")
	@Override
	public List<TreeNode<?,G>> children(TreeNode element) {
			return ((TreeNode) element).children();
	}
	

	@Override
	public TreeNode<?,G> parent(TreeNode<?,G> element) {
			return element.parent();
	}


}
