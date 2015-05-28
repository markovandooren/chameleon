package org.aikodi.chameleon.analysis.dependency;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.contract.Contracts;

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
}
