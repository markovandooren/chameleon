package org.aikodi.chameleon.ui.widget.list;

import org.aikodi.chameleon.ui.widget.SelectionController;

public interface ComboBoxController<W> extends SelectionController<W> {
	
	public void select(int baseOneIndex);

}
