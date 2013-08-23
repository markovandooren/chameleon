package be.kuleuven.cs.distrinet.chameleon.ui.widget;

import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public class PredicateContentProvider<T extends Element> extends TreeContentProvider<T> {

	public PredicateContentProvider(UniversalPredicate<T,Nothing> predicate) {
		super(predicate.type());
		_predicate = predicate;
	}
	
	private UniversalPredicate<T,Nothing> _predicate;
	
	public UniversalPredicate<T,Nothing> predicate() {
		return _predicate;
	}

	@Override
	public List<T> children(T element) {
		return element == null ? Collections.EMPTY_LIST : element.nearestDescendants(predicate());
	}

	@Override
	public T parent(T element) {
		return element == null ? null : element.nearestAncestor(predicate());
	}

	@Override
	public TreeViewerNode<?> createNode(Object input) {
		return null;
	}
}
