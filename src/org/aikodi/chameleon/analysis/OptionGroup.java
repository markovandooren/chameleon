package org.aikodi.chameleon.analysis;

import java.util.List;

import org.aikodi.chameleon.ui.widget.Selector;
import org.aikodi.chameleon.ui.widget.WidgetFactory;
import org.aikodi.chameleon.util.Lists;

public abstract class OptionGroup {

	public OptionGroup(String name) {
		_name = name;
	}
	
	public String name() {
		return _name;
	}
	
	private String _name;
	
	public void createControls(WidgetFactory<?> widgetFactory) {
		for(Selector selector:selectors()) {
			selector.createControl(widgetFactory);
		}
	}
	
	
	protected void add(Selector selector) {
		_selectors.add(selector);
	}
	
	private List<Selector> _selectors = Lists.create();

	protected List<? extends Selector> selectors() {
		return _selectors;
	}	
	
	public void setContext(Object context) {
		for(Selector selector: _selectors) {
			selector.setContext(context);
		}
	}
	
}
