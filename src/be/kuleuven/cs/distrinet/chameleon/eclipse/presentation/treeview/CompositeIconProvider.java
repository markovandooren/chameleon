package be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.treeview;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;

/**
 * A composite icon provider. Composite icon providers keep a list of 
 * icon providers and used these to find the icon for a given name.
 * The icon providers in the list are consulted in the order of the list.
 * 
 * @author Marko van Dooren
 *
 */
public class CompositeIconProvider implements IconProvider {

	
	/**
	 * Create an empty composite icon provider. 
	 */
 /*@
   @ public behavior
   @
   @ post providers().isEmpty();
   @*/
	public CompositeIconProvider() {
	}

	/**
	 * Create a compositie icon provider with the given list of icon providers.
	 * @param providers
	 */
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

	@Override
   public String iconName(Element element) throws ModelException {
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
