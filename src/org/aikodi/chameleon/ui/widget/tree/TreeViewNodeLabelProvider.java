package org.aikodi.chameleon.ui.widget.tree;

import org.aikodi.chameleon.ui.widget.LabelProvider;


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