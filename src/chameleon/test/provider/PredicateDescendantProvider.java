package chameleon.test.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.rejuse.predicate.SafePredicate;

import chameleon.core.element.Element;
import chameleon.workspace.Project;

/**
 * A class for provider descendants of type E that satisfy a given predicate,
 * given a provider for the ancestors of the elements.
 * 
 * @author Marko van Dooren
 *
 * @param <E> The type of the elements to be searched.
 */
public class PredicateDescendantProvider<E extends Element> extends AbstractDescendantProvider<E> {
	
	/**
	 * Create a new descendant provider with the given provider for ancestors,
	 * and a class object that represents that type of element to be found.
	 * @param ancestorProvider
	 */
	public PredicateDescendantProvider(ElementProvider<? extends Element> ancestorProvider, SafePredicate<E> predicate, Class<E> cls) {
		super(ancestorProvider, cls);
		_predicate = predicate;
	}

	private SafePredicate<E> _predicate;

	/**
	 * Return the predicate that must be satisfied by the provided elements.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public SafePredicate<E> predicate() {
		return _predicate;
	}
	
	public Collection<E> elements(Project project) {
		Collection<E> result = new ArrayList();
		Class<E> cls = elementType();
		SafePredicate<E> predicate = predicate();
		for(Element element: ancestorProvider().elements(project)) {
			result.addAll(element.descendants(cls, predicate));
		}
		return result;
	}
}
