package org.aikodi.chameleon.ui.widget.tree;

import org.aikodi.chameleon.ui.widget.SelectionController;

public interface TristateTreeController<W> extends SelectionController<W>{

//	public void setChecked(TreeNode node);
	public void setCheckStateProvider(CheckStateProvider provider);
	
}
