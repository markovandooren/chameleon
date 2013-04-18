package be.kuleuven.cs.distrinet.chameleon.oo.type;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ElementWithModifiersImpl;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.Modifier;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.member.Member;
import be.kuleuven.cs.distrinet.rejuse.property.Property;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;

/**
 * Support class for member-like elements that can be the direct children of a type.
 * 
 * @author Marko van Dooren
 */
public abstract class TypeElementImpl extends ElementWithModifiersImpl implements TypeElement {
  

//	public CheckedExceptionList getCEL() throws LookupException {
//	  return new CheckedExceptionList();	
//	}
//	
//	public CheckedExceptionList getAbsCEL() throws LookupException {
//		return new CheckedExceptionList();
//	}

	public abstract TypeElementImpl clone();

	public List<? extends Member> declaredMembers() {
    try {
			return getIntroducedMembers();
		} catch (LookupException e) {
			throw new ChameleonProgrammerException("This should not happen. Element of class "+this.getClass().getName()+" threw a lookup exception in getIntroducedMembers. This exception ended up in declaredMembers. But if that is the case, then declaredMembers must be overridden to provide a proper definition.", e);
		}
  }

  public List<Modifier> modifiers(PropertyMutex mutex) throws ModelException {
  	Property property = property(mutex);
  	List<Modifier> result = new ArrayList<Modifier>();
  	for(Modifier mod: modifiers()) {
  		if(mod.impliesTrue(property)) {
  			result.add(mod);
  		}
  	}
  	return result;
  }
}
