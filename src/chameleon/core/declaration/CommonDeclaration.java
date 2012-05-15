package chameleon.core.declaration;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.property.Property;
import org.rejuse.property.PropertyMutex;
import org.rejuse.property.PropertySet;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.modifier.ElementWithModifiers;
import chameleon.core.modifier.Modifier;
import chameleon.core.property.ChameleonProperty;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.exception.ModelException;
import chameleon.util.association.Multi;
import chameleon.util.association.Single;

/**
 * A convenience class for declarations with a fixed signature and optional modifiers.
 * 
 * @author Marko van Dooren
 */
public abstract class CommonDeclaration extends DeclarationImpl implements ElementWithModifiers {

	/**
	 * Instantiate a new common declaration without a signature.
	 */
	public CommonDeclaration() {
	}
	
	/**
	 * Instantiate a new common declaration with the given signature.
	 * 
	 * @param signature The signature of the declaration
	 */
	public CommonDeclaration(Signature signature) {
		setSignature(signature);
	}

	private Single<Signature> _signature = new Single<Signature>(this);

	@Override
	public Signature signature() {
		return _signature.getOtherEnd();
	}

	@Override
	public void setSignature(Signature signature) {
		set(_signature,signature);
	}
	
	@Override
	public LookupStrategy targetContext() throws LookupException {
		throw new LookupException("A goal does not contain declarations.");
	}

	public abstract CommonDeclaration clone();
	
  /*************
   * MODIFIERS *
   *************/
  private Multi<Modifier> _modifiers = new Multi<Modifier>(this);

  /**
   * Return the list of modifiers of this member.
   */
 /*@
   @ behavior
   @
   @ post \result != null;
   @*/
  public List<Modifier> modifiers() {
    return _modifiers.getOtherEnds();
  }

  /**
   * Add the given modifier to this element.
   * @param modifier The modifier to be added.
   */
 /*@
   @ public behavior
   @
   @ pre modifier != null;
   @
   @ post modifiers().contains(modifier);
   @*/
  public void addModifier(Modifier modifier) {
  	add(_modifiers,modifier);
  }
  
  /**
   * Add all modifiers in the given collection to this element.
   * 
   * @param modifiers The collection that contains the modifiers that must be added.
   */
 /*@
   @ public behavior
   @
   @ pre modifiers != null;
   @ pre ! modifiers.contains(null);
   @
   @ post modifiers().containsAll(modifiers);
   @*/
  public void addModifiers(List<Modifier> modifiers) {
  	if(modifiers == null) {
  		throw new ChameleonProgrammerException("List passed to addModifiers is null");
  	} else {
  		for(Modifier modifier: modifiers) {
  			addModifier(modifier);
  		}
  	}
  }

  /**
   * Remove the given modifier from this element.
   * 
   * @param modifier The modifier that must be removed.
   */
 /*@
   @ public behavior
   @
   @ pre modifier != null;
   @
   @ post ! modifiers().contains(modifier);
   @*/
  public void removeModifier(Modifier modifier) {
  	remove(_modifiers,modifier);
  }

  /**
   * Check whether this element contains the given modifier.
   * 
   * @param modifier The modifier that is searched.
   */
 /*@
   @ public behavior
   @
   @ pre modifier != null;
   @
   @ post \result == modifiers().contains(modifier);
   @*/
  public boolean hasModifier(Modifier modifier) {
    return _modifiers.getOtherEnds().contains(modifier);
  }

  public PropertySet<Element,ChameleonProperty> declaredProperties() {
    return myDeclaredProperties();
  }

	protected PropertySet<Element,ChameleonProperty> myDeclaredProperties() {
		PropertySet<Element,ChameleonProperty> result = new PropertySet<Element,ChameleonProperty>();
    for(Modifier modifier:modifiers()) {
      result.addAll(modifier.impliedProperties().properties());
    }
    return result;
	}

	/**
	 * Return the default properties for this element.
	 * @return
	 */
	protected PropertySet<Element,ChameleonProperty> myDefaultProperties() {
		return language().defaultProperties(this);
	}
	
	public List<Modifier> modifiers(Property property) throws ModelException {
		List<Modifier> result = new ArrayList<Modifier>();
		for(Modifier modifier: modifiers()) {
			if(modifier.impliesTrue(property)) {
				result.add(modifier);
			}
		}
		return result;
	}

	@Override
	public List<Modifier> modifiers(PropertyMutex mutex) throws ModelException {
		Property property = property(mutex);
		List<Modifier> result = new ArrayList<Modifier>();
		for (Modifier mod : modifiers()) {
			if (mod.impliesTrue(property)) {
				result.add(mod);
			}
		}
		return result;
	}
}