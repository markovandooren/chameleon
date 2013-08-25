package be.kuleuven.cs.distrinet.chameleon.ui.widget;

public interface TreeListener<V> {

	public void itemChanged(TreeNode<V> data, boolean checked, boolean grayed);
}
