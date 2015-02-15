package be.kuleuven.cs.distrinet.chameleon.core.property;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyUniverse;
import be.kuleuven.cs.distrinet.rejuse.property.StaticProperty;

import com.google.common.collect.ImmutableList;

public class StaticChameleonProperty extends StaticProperty<Element,ChameleonProperty> implements ChameleonProperty {

	public StaticChameleonProperty(String name, PropertyUniverse<ChameleonProperty> universe,
			PropertyMutex<ChameleonProperty> mutex, ChameleonProperty inverse, Class<? extends Element> validElementType) {
		super(name, universe, mutex, inverse);
		addValidElementType(validElementType);
	}

	public StaticChameleonProperty(String name, PropertyUniverse<ChameleonProperty> universe,
			PropertyMutex<ChameleonProperty> mutex, Class<? extends Element> validElementType) {
		super(name,universe, mutex);
		addValidElementType(validElementType);
	}

	public StaticChameleonProperty(String name, PropertyUniverse<ChameleonProperty> universe, Class<? extends Element> validElementType) {
		super(name, universe);
		addValidElementType(validElementType);
	}

	/**
	 * YOU MUST OVERRIDE THIS METHOD IF YOU CREATE A SUBCLASS. Otherwise, the verify() method of the inverse may
	 * not work properly.
	 */
	@Override
	protected void createInverse(String name, PropertyUniverse<ChameleonProperty> universe) {
		new InverseChameleonProperty("not "+name, universe, mutex(), this);
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
		private InverseChameleonProperty(String name, PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> family,
				ChameleonProperty inverse) {
			super(name, universe, family, inverse);
		}

		@Override
		protected void createInverse(String name, PropertyUniverse<ChameleonProperty> universe) {
			throw new ChameleonProgrammerException("This method should not be invoked for an InverseProperty. Its inverse is passed as an argument to the constructor.");
		}

		@Override
      public Verification verify(Element element) {
			final Class<? extends Element> elementClass = element.getClass();
			boolean validType = new SafePredicate<Class<? extends Element>>() {
				@Override
				public boolean eval(Class<? extends Element> validClass) {
					return validClass.isAssignableFrom(elementClass);
				}
			}.exists(_validTypes);
			if(validType) {
				return Valid.create();
			} else {
			  return new BasicProblem(element, "Property "+name()+" is not applicable to a "+elementClass.getName()); 
			}
		}

		@Override
		public void addValidElementType(Class<? extends Element> type) {
			StaticChameleonProperty.this.addValidElementType(type);
		}
	}


}
