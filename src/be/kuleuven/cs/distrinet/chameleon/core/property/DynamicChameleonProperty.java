package be.kuleuven.cs.distrinet.chameleon.core.property;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.rejuse.logic.ternary.Ternary;
import be.kuleuven.cs.distrinet.rejuse.property.DynamicProperty;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyUniverse;

import com.google.common.collect.ImmutableList;

public abstract class DynamicChameleonProperty extends DynamicProperty<Element,ChameleonProperty> implements ChameleonProperty {

	public DynamicChameleonProperty(String name, PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> mutex,
			ChameleonProperty inverse, Class<? extends Element> validElementType) {
		super(name, universe, mutex, inverse);
		addValidElementType(validElementType);
	}

	public DynamicChameleonProperty(String name, PropertyUniverse<ChameleonProperty> universe, PropertyMutex<ChameleonProperty> mutex, Class<? extends Element> validElementType) {
		super(name, universe, mutex);
		addValidElementType(validElementType);
	}

	public DynamicChameleonProperty(String name, PropertyUniverse<ChameleonProperty> universe, Class<? extends Element> validElementType) {
		this(name, universe, new PropertyMutex<ChameleonProperty>(), validElementType);
	}

	
  /**
   * An object is defined if and only if it is a Definition, and
   * it is complete. 
   */
 /*@
   @ behavior
   @
   @ post \result == (element instanceof Definition) && ((Definition)element).complete();
   @*/
  @Override public Ternary appliesTo(Element element) {
		PropertySet<Element,ChameleonProperty> declared = element.declaredProperties();
		Ternary result = declared.implies(this);
		if(result == Ternary.UNKNOWN) {
			result = selfAppliesTo(element);
		}
    return result;
  }

  protected abstract Ternary selfAppliesTo(Element element);

	private final class InverseDynamicChameleonProperty extends InverseProperty<Element, ChameleonProperty> implements ChameleonProperty {
		private InverseDynamicChameleonProperty(String name, PropertyUniverse<ChameleonProperty> universe,
				PropertyMutex<ChameleonProperty> mutex, ChameleonProperty inverse) {
			super(name, universe, mutex, inverse);
		}

		@Override
		protected void createInverse(String name, PropertyUniverse<ChameleonProperty> universe) {
		}

		public Verification verify(Element element) {
			return verifyAux(element, this);
		}

		@Override
		public void addValidElementType(Class<? extends Element> type) {
			DynamicChameleonProperty.this.addValidElementType(type);
		}
	}

	@Override
	protected void createInverse(String name, PropertyUniverse<ChameleonProperty> universe) {
		new InverseDynamicChameleonProperty("not "+name, universe, mutex(), this);
	}

	public Verification verify(Element element) {
		return verifyAux(element,this);
	}
	
	public Verification verifyAux(Element element, DynamicProperty<Element,ChameleonProperty> property) {
		Verification result = Valid.create();

		final Class<? extends Element> elementClass = element.getClass();
		boolean validType = false;
		for(Class<? extends Element> validClass: _validTypes) {
				if(validClass.isAssignableFrom(elementClass)) {
					validType = true;
					break;
				}
		};
		if(! validType) {
		  result = result.and(new BasicProblem(element, "Property "+property.name()+" is not applicable to a "+elementClass.getName())); 
		}

		Ternary appliesTo = property.appliesTo(element);
		if(appliesTo == Ternary.FALSE) {
			result = result.and(new BasicProblem(element,"Property "+property.name()+" does not apply to the element"));
		} else if(appliesTo == Ternary.UNKNOWN) {
			result = result.and(new BasicProblem(element,"It is not known if property "+property.name()+" applies to the element"));
		}
		return result;
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
	public void addValidElementType(Class<? extends Element> type) {
		_validTypes = _builder.add(type).build();
	}
	
	private ImmutableList.Builder<Class<? extends Element>> _builder = ImmutableList.builder();
	
	private ImmutableList<Class<? extends Element>> _validTypes = _builder.build();


}
