package be.kuleuven.cs.distrinet.chameleon.ui.widget.tree;

import be.kuleuven.cs.distrinet.chameleon.ui.widget.SelectionController;

public interface TristateTreeController<W> extends SelectionController<W>{

//	public void setChecked(TreeNode node);
	public void setCheckStateProvider(CheckStateProvider provider);
	
}
