package chameleon.eclipse.presentation.treeview;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.element.Element;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.exception.ModelException;

public class CompositeIconProvider implements IconProvider {

	
	public CompositeIconProvider() {
		
	}
	
	public CompositeIconProvider(IconProvider... providers) {
		for(IconProvider provider:providers) {
			add(provider);
		}
	}
	
	public void add(IconProvider provider) {
		if(provider == null) {
			throw new ChameleonProgrammerException();
		}
		_providers.add(provider);
	}
	
	public void remove(IconProvider provider) {
		_providers.remove(provider);
	}
	
	public List<IconProvider> providers() {
		return _providers;
	}
	
	private List<IconProvider> _providers = new ArrayList<IconProvider>();

	public String iconName(Element<?> element) throws ModelException {
		String result = null;
		for(IconProvider provider: _providers) {
			result = provider.iconName(element);
			if(result != null) {
				break;
			}
		}
		return result;
	}
}
