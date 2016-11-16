package org.aikodi.chameleon.analysis.dependency;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.contract.Contracts;
import org.aikodi.rejuse.action.Nothing;
import org.aikodi.rejuse.function.Function;
import org.aikodi.rejuse.predicate.Predicate;

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
	
	
	public static interface FinderWithoutSource extends Predicate<CrossReference<?>,LookupException> {
		public Selector<? extends Predicate<? super CrossReference<?>,LookupException>,CrossReference<?>,LookupException> from();
	}

	public static interface FinderWithoutTarget extends Predicate<CrossReference<?>,LookupException> {
		public Selector<? extends Predicate<? super CrossReference<?>,LookupException>,Declaration,LookupException> to();
	}

	
	public static class DependencyFinder implements FinderWithoutSource, FinderWithoutTarget {
		
		public Selector<FinderWithoutTarget,CrossReference<?>,LookupException> from() {
			return new Selector<>(p -> new DependencyFinderWithSource(p));
		}
		
		public Selector<FinderWithoutSource,Declaration,LookupException> to() {
			return new Selector<>(p -> new DependencyFinderWithTarget(p));
		}

		@Override
		public boolean eval(CrossReference<?> object) {
			return true;
		}
		
	}
	
	public static class DependencyFinderWithSource implements FinderWithoutTarget {
		private Predicate<? super CrossReference<?>, ? extends LookupException> _from;

		public DependencyFinderWithSource(Predicate<? super CrossReference<?>, ? extends LookupException> from) {
			if(from == null) {
				throw new IllegalArgumentException();
			}
			this._from = from;
		}
		
		public Selector<? extends Predicate<? super CrossReference<?>,LookupException>,Declaration,LookupException> to() {
			return new Selector<>(p -> new DependencyFinderWithBoth(_from, p));
		}

		@Override
		public boolean eval(CrossReference<?> object) throws LookupException {
			return _from.eval(object);
		}

	}
	
	public static class DependencyFinderWithTarget implements FinderWithoutSource {
		private Predicate<? super Declaration, ? extends LookupException> _to;

		public DependencyFinderWithTarget(Predicate<? super Declaration, ? extends LookupException> to) {
			if(to == null) {
				throw new IllegalArgumentException();
			}
			this._to = to;
		}
		
		public Selector<? extends Predicate<? super CrossReference<?>,LookupException>,CrossReference<?>,LookupException> from() {
			return new Selector<>(p -> new DependencyFinderWithBoth(p, _to));
		}

		@Override
		public boolean eval(CrossReference<?> object) throws LookupException {
			return _to.eval(object.getElement());
		}

	}

	public static class DependencyFinderWithBoth implements Predicate<CrossReference<?>,LookupException> {
		private Predicate<? super Declaration,? extends LookupException> _to;
		private Predicate<? super CrossReference<?>,? extends LookupException> _from;

		public boolean eval(CrossReference<?> element) throws LookupException {
			return _from.eval(element) && _to.eval(element.getElement());
		}

		public DependencyFinderWithBoth(Predicate<? super CrossReference<?>,? extends LookupException> from, Predicate<? super Declaration,? extends LookupException> to) {
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
		Predicate<CrossReference<?>,LookupException> predicate = new DependencyFinder()
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
	public static class Selector<B extends Predicate<CrossReference<?>,LookupException>,E extends Element, X extends Exception> {
		
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
