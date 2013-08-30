package be.kuleuven.cs.distrinet.chameleon.core.element;

import java.util.List;

import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public abstract class TreeNavigator<T> {
	
	public abstract T parent(T element);
	
	public abstract List<? extends T> children(T element);
	
	public abstract TreeNavigator<T> tree(T element);
	
	public <X extends T, E extends Exception> X nearestAncestor(T element, UniversalPredicate<X,E> predicate) throws E {
		T el = parent(element);
		while ((el != null) && (! predicate.eval((T)el))) {
			el = tree(el).parent(el);
		}
		return (X) el;
	}

	public <X extends T, E extends Exception> X nearestAncestorOrSelf(T element, UniversalPredicate<X, E> predicate) throws E {
		T el = element;
		while ((el != null) && (! predicate.eval((T)el))) {
			el = tree(el).parent(el);
		}
		return (X) el;
	}

}