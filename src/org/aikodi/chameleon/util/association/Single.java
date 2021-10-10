package org.aikodi.chameleon.util.association;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.rejuse.association.Association;
import org.aikodi.rejuse.association.SingleAssociation;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Single<T extends Element> extends SingleAssociation<Element, T> implements ChameleonAssociation<T> {

	public Single(Element element) {
		super(element);
	}

	public Single(Element element, Class<T> type, String role) {
		super(element);
		setRole(role);
		_type = type;
	}
	
	private Class<T> _type;

	@Override
	protected boolean isValidElement(Association<? extends T, ? super Element> relation) {
		boolean result = super.isValidElement(relation) && 
				((_type == null || relation == null || _type.isInstance(relation.getObject())));
		return result;
	}
	
	/**
	 * Create a new single association object for the given element.
	 * The object that this association can be connected to has a role
	 * with the given name.
	 *  
	 * @param element The element that contains this association.
	 *                The element cannot be null.
	 * @param role The role of the object that can be connected via this association.
	 */
	public Single(Element element, String role) {
		super(element);
		setRole(role);
	}

	public Single(Element element, boolean mandatory, String role) {
		this(element, role);
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
	
	private void setRole(String role) {
		if(role == null) {
			throw new IllegalArgumentException();
		}
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
