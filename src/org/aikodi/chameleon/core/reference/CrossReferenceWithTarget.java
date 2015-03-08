package org.aikodi.chameleon.core.reference;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;

/**
 * A cross-reference that can have a target.
 * 
 * If a cross-reference has a target, then the target is look up first, and
 * the declaration referenced by this cross-reference is looked up in that
 * target. Otherwise, the the declaration referenced by this cross-reference is 
 * looked up in the {@link Element#lexicalContext()} of this cross-reference.
 * 
 * @author Marko van Dooren
 *
 * @param <D>
 */
public interface CrossReferenceWithTarget<D extends Declaration> extends CrossReference<D> {
	
   /**
    * @return The target of this cross-reference, if any. The target is
    * a cross-reference that references a declaration in which the declaration
    * reference by this cross-reference is looked up.
    */
	public CrossReferenceTarget getTarget();
	
	/**
	 * Set the target of this cross-reference.
	 * 
	 * @param target The new target of this cross-reference.
	 */
	public void setTarget(CrossReferenceTarget target);

}
