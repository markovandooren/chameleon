package be.kuleuven.cs.distrinet.chameleon.analysis;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.ui.widget.Selector;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.WidgetFactory;

public abstract class OptionGroup {

	public OptionGroup(String name) {
		_name = name;
	}
	
	public String name() {
		return _name;
	}
	
	private String _name;
	
	public void createControls(WidgetFactory<?> widgetFactory) {
		for(Selector<?> selector:selectors()) {
			selector.createControl(widgetFactory);
		}
	}
	
	
	protected void add(Selector<?> selector) {
		_selectors.add(selector);
	}
	
	private List<Selector<?>> _selectors = new ArrayList<>();

	protected List<? extends Selector<?>> selectors() {
		return _selectors;
	}	
	
	public void setContext(Object context) {
		for(Selector<?> selector: _selectors) {
			selector.setContext(context);
		}
	}
	
}
