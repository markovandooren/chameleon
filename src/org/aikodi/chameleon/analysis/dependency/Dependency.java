package org.aikodi.chameleon.analysis.dependency;

import org.aikodi.chameleon.analysis.dependency.Dependency.DependencyFinderWithBoth;
import org.aikodi.chameleon.analysis.dependency.Dependency.DependencyFinderWithSource;
import org.aikodi.chameleon.analysis.dependency.Dependency.DependencyFinderWithTarget;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.contract.Contracts;
import org.eclipse.swt.internal.C;

import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.function.Function;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;

/**
 * A dependency between a source element and a target element.
 * 
 * @author Marko van Dooren
 *
 * @param <S> The type of the source element.
 * @param <C> The type of the cross-reference.
 * @param <T> The type of the target element.
 */
public class Dependency<S extends Element,C extends CrossReference,T extends Element> {

  /**
   * Create a new dependency with the given source, cross-reference, and target.
   * 
   * @param source The element that depends on the given target. The source
   *               cannot be null.
   * @param crossReference The cross-reference that caused the dependency. The
   *               cross-reference cannot be null.
   * @param target The element on which the given source element depends 
   *               because of the given cross-reference. The target cannot be
   *               null.
   */
	public Dependency(S source, C crossReference, T target) {
		Contracts.notNull(source, "The source of a dependency cannot be null.");
    Contracts.notNull(crossReference, "The cross-reference of a dependency cannot be null.");
    Contracts.notNull(target, "The target of a dependency cannot be null.");
		this._source = source;
		this._crossReference = crossReference;
		this._target = target;
	}

	private S _source;
	
	/**
	 * @return The element that depends on the given target. The source
   *         is not null.
	 */
	public S source() {
		return _source;
	}

	private C _crossReference;
	
	/**
	 * @return The cross-reference that caused the dependency. The
   *               cross-reference is not null.
	 */
	public C crossReference() {
		return _crossReference;
	}

	/**
	 * @return The element on which the source element depends because of the 
	 *         the cross-reference of this dependency. The target is not null.
	 */
	public T target() {
		return _target;
	}

	private T _target;
	
	
	public static interface Finder<X extends Exception> extends Predicate<CrossReference<?>,X> {
	}
	
	public static interface FinderWithoutSource<X extends Exception> extends Finder<X> {
		public Selector<? extends Finder<X>,CrossReference<?>,X> from();
	}

	public static interface FinderWithoutTarget<X extends Exception> extends Finder<X> {
		public Selector<? extends Finder<X>,Declaration,X> to();
	}

	
	public static class DependencyFinder<X extends Exception> implements FinderWithoutSource<X>, FinderWithoutTarget<X> {
		
		public Selector<FinderWithoutTarget<X>,CrossReference<?>,X> from() {
			return new Selector<>(p -> new DependencyFinderWithSource<X>(p));
		}
		
		public Selector<FinderWithoutSource<X>,Declaration,X> to() {
			return new Selector<>(p -> new DependencyFinderWithTarget<X>(p));
		}

		@Override
		public boolean eval(CrossReference<?> object) throws X {
			return true;
		}
		
	}
	
	public static class DependencyFinderWithSource<X extends Exception> implements FinderWithoutTarget<X> {
		private Predicate<? super CrossReference<?>, ? extends X> _from;

		public DependencyFinderWithSource(Predicate<? super CrossReference<?>, ? extends X> from) {
			if(from == null) {
				throw new IllegalArgumentException();
			}
			this._from = from;
		}
		
		public Selector<Finder<X>,Declaration,X> to() {
			return new Selector<>(p -> new DependencyFinderWithBoth<X>(_from, p));
		}

		@Override
		public boolean eval(CrossReference<?> object) throws X {
			return _from.eval(object);
		}

	}
	
	public static class DependencyFinderWithTarget<X extends Exception> implements FinderWithoutSource<X> {
		private Predicate<? super Declaration, ? extends X> _to;

		public DependencyFinderWithTarget(Predicate<? super Declaration, ? extends X> to) {
			if(to == null) {
				throw new IllegalArgumentException();
			}
			this._to = to;
		}
		
		public Selector<Finder<X>,CrossReference<?>,X> from() {
			return new Selector<>(p -> new DependencyFinderWithBoth<X>(p, _to));
		}

		@Override
		public boolean eval(CrossReference<?> object) throws X {
			return _to.eval(object.getElement());
		}

	}

	public static class DependencyFinderWithBoth<X extends Exception> implements Finder<X> {
		private Predicate<? super Declaration,? extends X> _to;
		private Predicate<? super CrossReference<?>,? extends X> _from;

		public boolean eval(CrossReference<?> element) throws X, LookupException {
			return _from.eval(element) && _to.eval(element.getElement());
		}

		public DependencyFinderWithBoth(Predicate<? super CrossReference<?>,? extends X> from, Predicate<? super Declaration,? extends X> to) {
			if(to == null) {
				throw new IllegalArgumentException();
			}
			if(from == null) {
				throw new IllegalArgumentException();
			}
			this._from = from;
			this._to = to;
		}
	}
	
	private void m() {
		Finder<LookupException> predicate = new DependencyFinder<LookupException>()
		  .from().inside(Type.class)
		  .to().predicate(Method.class, m -> m.name().length() > 3);
	}
	
	
	
	/**
	 * 
	 * @author Marko van Dooren
	 *
	 * @param <B> The builder that is returned.
	 * @param <E> The type of element on which the selection is done.
	 */
	//FIXME: B must determine the upper bound for X
	public static class Selector<B extends Finder<X>,E extends Element, X extends Exception> {
		
		private Function<Predicate<? super E,? extends X>,B,Nothing> function;
		
		public Selector(Function<Predicate<? super E,? extends X>, B,Nothing> function) {
			if(function == null) {
				throw new IllegalArgumentException();
			}
			this.function = function;
		}

		public <C extends Element> B inside(Class<C> kind) {
			return function.apply(e -> e.nearestAncestor(kind) != null);
		}
		
//		public <C extends E> B type(Class<C> kind) {
//			return function.apply(e -> kind.isInstance(e));
//		}
		
		public B predicate(Predicate<? super E,? extends X> predicate) {
			//FIXME: nearestAncestor
			return function.apply(e -> predicate.eval(e));
		}
		
		public <C extends E> B predicate(Class<C> kind, Predicate<? super C,? extends X> predicate) {
			return function.apply(e -> kind.isInstance(e) && predicate.eval((C) e));
		}

		public B same(E e) {
			return function.apply(x -> x.equals(e));
		}
	}
}
