package be.kuleuven.cs.distrinet.chameleon.ui.widget.list;

import be.kuleuven.cs.distrinet.chameleon.ui.widget.SelectionController;

public interface ComboBoxController<W> extends SelectionController<W> {
	
	public void select(int baseOneIndex);

}
