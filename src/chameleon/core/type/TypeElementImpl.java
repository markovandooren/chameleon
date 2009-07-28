package chameleon.core.type;

import java.util.List;

import org.rejuse.association.OrderedReferenceSet;
import org.rejuse.property.PropertySet;

import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.modifier.Modifier;
import chameleon.core.namespacepart.NamespaceElementImpl;
import chameleon.core.statement.CheckedExceptionList;

/**
 * Support class for member-like elements that can be the direct children of a type.
 * 
 * @author Marko van Dooren
 *
 * @param <E> The type of the element
 * @param <P> The type of the parent
 */
public abstract class TypeElementImpl<E extends TypeElementImpl<E,P>, P extends Element> extends NamespaceElementImpl<E, P> implements TypeElement<E,P> {
  
  /*************
   * MODIFIERS *
   *************/
  private OrderedReferenceSet<TypeElement, Modifier> _modifiers = new OrderedReferenceSet<TypeElement, Modifier>(this);

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

  public void addModifier(Modifier modifier) {
    if (modifier != null) {
    	if (!_modifiers.contains(modifier.parentLink())) {
    		_modifiers.add(modifier.parentLink());	
      }
    } else {
    	throw new ChameleonProgrammerException("Modifier passed to addModifier is null");
    }
  }
  
  public void addModifiers(List<Modifier> modifiers) {
  	if(modifiers == null) {
  		throw new ChameleonProgrammerException("List passed to addModifiers is null");
  	} else {
  		for(Modifier modifier: modifiers) {
  			addModifier(modifier);
  		}
  	}
  }

  public void removeModifier(Modifier modifier) {
  	if(modifier != null) {
      _modifiers.remove(modifier.parentLink());
  	} else {
  		throw new ChameleonProgrammerException("Modifier passed to removeModifier was null");
  	}
  }

  public boolean hasModifier(Modifier modifier) {
    return _modifiers.getOtherEnds().contains(modifier);
  }

  public PropertySet<Element> declaredProperties() {
    return myDeclaredProperties();
  }

	protected PropertySet<Element> myDeclaredProperties() {
		PropertySet<Element> result = new PropertySet<Element>();
    for(Modifier modifier:modifiers()) {
      result.addAll(modifier.impliedProperties().properties());
    }
    return result;
	}

	protected PropertySet<Element> myDefaultProperties() {
		return language().defaultProperties(this);
	}

	public CheckedExceptionList getCEL() throws LookupException {
	  return new CheckedExceptionList();	
	}
	
	public CheckedExceptionList getAbsCEL() throws LookupException {
		return new CheckedExceptionList();
	}

	
	
}
