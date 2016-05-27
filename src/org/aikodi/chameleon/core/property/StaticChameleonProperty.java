package org.aikodi.chameleon.core.property;

import static be.kuleuven.cs.distrinet.rejuse.collection.CollectionOperations.exists;

import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;

import be.kuleuven.cs.distrinet.rejuse.collection.CollectionOperations;
import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyUniverse;
import be.kuleuven.cs.distrinet.rejuse.property.StaticProperty;

import com.google.common.collect.ImmutableList;

public class StaticChameleonProperty extends StaticProperty<Element,ChameleonProperty> implements ChameleonProperty {

	public StaticChameleonProperty(String name, 
			PropertyMutex<ChameleonProperty> mutex, ChameleonProperty inverse, Class<? extends Element> validElementType) {
		super(name, mutex, inverse);
		addValidElementType(validElementType);
	}

	public StaticChameleonProperty(String name,
			PropertyMutex<ChameleonProperty> mutex, Class<? extends Element> validElementType) {
		super(name, mutex);
		addValidElementType(validElementType);
	}

	public StaticChameleonProperty(String name, Class<? extends Element> validElementType) {
		super(name);
		addValidElementType(validElementType);
	}

	/**
	 * YOU MUST OVERRIDE THIS METHOD IF YOU CREATE A SUBCLASS. Otherwise, the verify() method of the inverse may
	 * not work properly.
	 */
	@Override
	protected void createInverse(String name) {
		new InverseChameleonProperty("not "+name, mutex(), this);
	}

	/**
	 * If the given element is not of a valid type for this property, a problem will be returned.
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @
   @ post ! (\exists Class<? extends Element> cls; validElementTypes().contains(cls); cls.isAssignableFrom(element.getClass()) ==> \result instanceof Invalid;
   @*/
	@Override
   public Verification verify(Element element) {
		final Class<? extends Element> elementClass = element.getClass();
		boolean validType = false;
		for(Class<? extends Element> validClass: _validTypes) {
				if(validClass.isAssignableFrom(elementClass)) {
					validType = true;
					break;
				}
		};
		if(validType) {
			return Valid.create();
		} else {
		  return new BasicProblem(element, "Property "+name()+" is not applicable to a "+elementClass.getName()); 
		}
	}
	
	/**
	 * Return a list of class files representing elements that can have this property.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post ! \result.contains(null);
   @*/
	public List<Class<? extends Element>> validElementTypes() {
		return _validTypes;
	}
	
 /**
	* Add the given class as a valid element type for this property. 
	*/
/*@
  @ public behavior
  @
  @ pre type != null;
  @ pre ! validElementTypes().contains(type);
  @
  @ post validElementTypes().contains(type);
  @*/
	@Override
   public void addValidElementType(Class<? extends Element> type) {
		_validTypes = _builder.add(type).build();
	}
	
	private ImmutableList.Builder<Class<? extends Element>> _builder = ImmutableList.builder();
	
	private ImmutableList<Class<? extends Element>> _validTypes = _builder.build();

	private final class InverseChameleonProperty extends InverseProperty<Element, ChameleonProperty> implements ChameleonProperty {
		private InverseChameleonProperty(String name, PropertyMutex<ChameleonProperty> family,
				ChameleonProperty inverse) {
			super(name, family, inverse);
		}

		@Override
		protected void createInverse(String name) {
			throw new ChameleonProgrammerException("This method should not be invoked for an InverseProperty. Its inverse is passed as an argument to the constructor.");
		}

		@Override
      public Verification verify(Element element) {
		  Verification result = Valid.create();
			if(! exists(_validTypes, t -> t.isAssignableFrom(element.getClass()))) {
        result = new BasicProblem(element, "Property "+name()+" is not applicable to a "+element.getClass().getName()); 
			}
			return result;
		}

		@Override
		public void addValidElementType(Class<? extends Element> type) {
			StaticChameleonProperty.this.addValidElementType(type);
		}
	}


}
