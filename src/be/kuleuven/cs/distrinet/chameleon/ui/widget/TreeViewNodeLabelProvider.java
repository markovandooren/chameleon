package be.kuleuven.cs.distrinet.chameleon.ui.widget;


public final class TreeViewNodeLabelProvider implements LabelProvider {

	@Override
	public String text(Object object) {
		String result = null;
		if(object instanceof TreeNode) {
			result = ((TreeNode) object).label();
		}
		return result;
	}
}