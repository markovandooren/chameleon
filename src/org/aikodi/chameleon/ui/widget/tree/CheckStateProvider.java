package org.aikodi.chameleon.ui.widget.tree;

public interface CheckStateProvider<V> {

	boolean isGrayed(V element);
	
	boolean isChecked(V element);
}
