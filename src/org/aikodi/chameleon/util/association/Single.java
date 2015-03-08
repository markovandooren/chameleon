package org.aikodi.chameleon.util.association;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;

import be.kuleuven.cs.distrinet.rejuse.association.Association;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;

public class Single<T extends Element> extends SingleAssociation<Element, T> implements ChameleonAssociation<T> {

	public Single(Element element) {
		super(element);
	}

	public Single(Element element, Class<T> type) {
		super(element);
		_type = type;
	}
	
	private Class<T> _type;

	@Override
	protected boolean isValidElement(Association<? extends T, ? super Element> relation) {
		boolean result = super.isValidElement(relation) && ((_type == null || relation == null || _type.isInstance(relation.getObject())));
//		Util.debug(! result);
		return result;
	}
	
	public Single(Element element, String role) {
		super(element);
		setRole(role);
	}

	public Single(Element element, Association<? extends T, ? super Element> other) {
		super(element, other);
	}
	
	public Single(Element element, boolean mandatory) {
		this(element);
		_mandatory = mandatory;
	}

	public Single(Element element, boolean mandatory, String role) {
		this(element, role);
		_mandatory = mandatory;
	}

	public Single(Element element, Association<? extends T, ? super Element> other, boolean mandatory) {
		this(element, other);
		_mandatory = mandatory;
	}
	
	private boolean _mandatory = false;
	
	public boolean mandatory() {
		return _mandatory;
	}
	
	@Override
   public Verification verify() {
		Verification result = Valid.create();
		if(mandatory()) {
			if(mandatory() && getOtherEnd() == null) {
				result = result.and(new BasicProblem(getObject(), "One " + role() + " was expected, but none is defined."));
			}
		}
		return result;
	}

	@Override
   public String role() {
		return _role;
	}
	
	public void setRole(String role) {
		_role = role;
	}
	
	private String _role = "element";
	
	@Override
	public T getOtherEnd() {
		T result = explicitElement();
		if(result == null) {
			result = implicitElement();
		}
		return result;
	}
	
	protected T explicitElement() {
		return super.getOtherEnd();
	}
	
	/**
	 * Return the element that is implicitly connected via this association. The
	 * result should only be non-null when there is no explicitly connected element.
	 * @return
	 */
	public T implicitElement() {
		return null;
	}

	/**
	 * Copy the contents of this association to the given other association.
	 * @param other
	 */
	public void cloneTo(ChameleonAssociation<T> o) {
		Single<T> other = (Single<T>) o;
		T otherEnd = explicitElement();
		if(otherEnd != null) {
			other.connectTo((Association) otherEnd.clone().parentLink());
		}
	}

	@Override
	public int size() {
		return getOtherEnd() == null ? 0 : 1;
	}
	
	@Override
	public void pairWise(ChameleonAssociation<?> other, BiConsumer<Element, Element> consumer) {
	   if(other instanceof Single) {
	      consumer.accept(this.getOtherEnd(), ((Single)other).getOtherEnd());
	   } else {
	      throw new IllegalArgumentException("The given association is not of the correct type.");
	   }
	}
	
	@Override
   public void mapTo(ChameleonAssociation<?> other, Function<Element, Element> mapper) {
      if(other instanceof Single) {
         T otherEnd = this.getOtherEnd();
         if(otherEnd != null) {
            mapper.apply(otherEnd).parentLink().connectTo((Single)other);
         }
      } else {
         if(other == null) {
            throw new IllegalArgumentException("The given association is null.");
         } else {
            throw new IllegalArgumentException("The given association is of the incorrect type. Expected: "+getClass().getName()+", but received: "+other.getClass().getName());
         }
      }
	}
}
