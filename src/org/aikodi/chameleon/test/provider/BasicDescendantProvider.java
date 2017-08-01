package org.aikodi.chameleon.test.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.workspace.View;

/**
 * A class for searching elements of type E in the elements provided by a provider to the ancestor
 * elements.
 * 
 * @author Marko van Dooren
 *
 * @param <E>
 */
public class BasicDescendantProvider<E extends Element> extends AbstractDescendantProvider<E> {
	
	/**
	 * Create a new basic descendant provider with the given ancestor provider, and the class
	 * object representing the type of the elements that must be provided.
	 */
 /*@
   @ public behavior
   @
   @ pre ancestorProvider != null;
   @ pre cls != null;
   @
   @ post ancestorProvider() == ancestorProvider;
   @ post selectedType() == cls;
   @*/
	public BasicDescendantProvider(ElementProvider<? extends Element> ancestorProvider, Class<E> cls) {
		super(ancestorProvider, cls);
	}

	/**
	 * Return a collection containing all descendants of type E of all elements
	 * provided by the ancestor provider.
	 */
 /*@
   @ public behavior
   @
   @ post (\forall Element ancestor; ancestorProvider().elements().contains(ancestor);
   @         \result.containsAll(ancestor.descendants(selectedType())));
  */
	@Override
   public Collection<E> elements(View project) {
		Collection<E> result = new ArrayList<E>();
		Class<E> cls = elementType();
		Collection<? extends Element> elements = ancestorProvider().elements(project);
		for(Element element: elements) {
			result.addAll(element.lexical().descendants(cls));
		}
		return result;
	}
}
