package org.aikodi.chameleon.ui.widget.tree;



public interface TreeListener<V> {

	public void itemChanged(TreeNode<?,V> data, boolean checked, boolean grayed);
}
