/**
 * 
 */
package org.aikodi.chameleon.oo.type;

import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.plugin.ObjectOrientedFactory;
import org.aikodi.chameleon.oo.type.generics.ElementWithTypeConstraints;
import org.aikodi.chameleon.oo.type.generics.TypeConstraint;
import org.aikodi.chameleon.util.association.Multi;

/**
 * @author Marko van Dooren
 *
 */
public class ConstrainedTypeReference extends ElementImpl implements TypeReference, ElementWithTypeConstraints {

	protected Multi<TypeConstraint> _typeConstraints = new Multi<TypeConstraint>(this);

	@Override
	public Type getElement() throws LookupException {
	  final Type constrainedType = language().plugin(ObjectOrientedFactory.class).createConstrainedType(lowerBound(), upperBound(),this);
	  return constrainedType;
	}

	/**
	 * 
	 */
	public ConstrainedTypeReference() {
	super();
	}

	public List<TypeConstraint> constraints() {
	  return _typeConstraints.getOtherEnds();
	}

	public void addConstraint(TypeConstraint constraint) {
	  add(_typeConstraints,constraint);
	}

	@Override
	protected synchronized void flushLocalCache() {
	  super.flushLocalCache();
	  _typeConstraints.flushCache();
	}

  @Override
  protected ConstrainedTypeReference cloneSelf() {
    return new ConstrainedTypeReference();
  }

	public String toString(java.util.Set<Element> visited) {
		StringBuilder builder = new StringBuilder();
		constraints().forEach(c -> builder.append(c.toString(visited)).append(" "));
		return builder.toString();
	}

}