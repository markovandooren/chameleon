package chameleon.core.namespace;

import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.reference.ElementReference;
import chameleon.core.relation.WeakPartialOrder;
import chameleon.core.type.Type;
import chameleon.util.Util;

/**
 * Generic Parameter R is the type of the referenced element.
 * @author marko
 */
public class NamespaceOrTypeReference<E extends NamespaceOrTypeReference, R extends NamespaceOrType> extends ElementReference<E,R,Element> {
  
 /*@
   @ public behavior
   @
   @ pre qn != null;
   @
   @ post getTarget() == getTarget(Util.getAllButLastPart(qn));
   @ post getName() == Util.getLastPart(qn);
   @*/
  public NamespaceOrTypeReference(String qn) {
    this(getTarget(Util.getAllButLastPart(qn)), Util.getLastPart(qn));
  }
  
 /*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post getTarget() == target;
   @ post getName() == name;
   @*/
  public NamespaceOrTypeReference(NamespaceOrTypeReference target, String name) {
  	super(name);
	  setTarget(target); 
  }
  
  /**
   * Return the fully qualified name of this namespace or type reference.
   * @return
   */
 /*@
   @ public behavior
   @
   @ post getTarget() == null ==> \result.equals(getName());
   @ post getTarget() != null ==> \result.equals(getTarget().fqn()+"."+getName());
   @*/
  public String getFullyQualifiedName() {
  	if(getTarget() == null) {
  		return getName();
  	} else {
  		return getTarget().getFullyQualifiedName()+"."+getName();
  	}
  }
  
  protected static NamespaceOrTypeReference getTarget(String qn) {
    if(qn == null) {
      return null;
    }
    NamespaceOrTypeReference target = new NamespaceOrTypeReference(null, Util.getFirstPart(qn));
    qn = Util.getSecondPart(qn);
    while(qn != null) {
    	NamespaceOrTypeReference newTarget = new NamespaceOrTypeReference(null, Util.getFirstPart(qn));
      newTarget.setTarget(target);
      target = newTarget;
      qn = Util.getSecondPart(qn);
    }
    return target;
  }
  

  
	/**
	 * TARGET
	 */
	private Reference<NamespaceOrTypeReference,NamespaceOrTypeReference> _target = new Reference<NamespaceOrTypeReference,NamespaceOrTypeReference>(this);

	protected Reference<NamespaceOrTypeReference,NamespaceOrTypeReference> targetLink() {
		return _target;
	}
	
  public NamespaceOrTypeReference getTarget() {
    return (NamespaceOrTypeReference)_target.getOtherEnd();
  }

  public void setTarget(NamespaceOrTypeReference target) {
    if(target != null) {
      _target.connectTo(target.parentLink());
    } else {
      _target.connectTo(null); 
    }
  }

 /*@
   @ also public behavior
   @
   @ post \result == Util.createNonNullList(getTarget());
   @*/
  public List children() {
    return Util.createNonNullList(getTarget());
  }

 /*@
   @ also public behavior
   @
   @ post getTarget() == null ==> \result == getContext(this).findPackageOrType(getName());
   @ post getTarget() != null ==> (
   @     (getTarget().getPackageOrType() == null ==> \result == null) &&
   @     (getTarget().getPackageOrType() == null ==> \result == 
   @         getTarget().getPackageOrType().getTargetContext().findPackageOrType(getName()));
   @*/
  public NamespaceOrType getNamespaceOrType() throws LookupException {
    NamespaceOrType result;
    
    //OPTIMISATION
    result = getCache();
    if(result != null) {
    	lookupLogger().debug("Hit cache for" + getFullyQualifiedName());
    	return result;
    }
    
    if(getTarget() != null) {
    	NamespaceOrType target = getTarget().getNamespaceOrType();
      
      if(target != null) {
        result = target.targetContext().lookUp(selector());
      } else {
      	throw new LookupException("Lookup of target of NamespaceOrVariableReference returned null",getTarget());
      }
    }
    else {
      result = lexicalContext().lookUp(selector());
    }
    if(result != null) {
    	//OPTIMISATION
    	setCache((R) result);
      return result;
    } else {
    	// repeat lookups for debugging purposes
    	if(getTarget() != null) {
      	NamespaceOrType target = getTarget().getNamespaceOrType();
        
        if(target != null) {
          result = target.targetContext().lookUp(selector());
        }
    	} else {
    		result = lexicalContext().lookUp(selector());
    	}
      throw new LookupException("Cannot find namespace or type with name: "+getName(),this);
    }
  }
  
  public DeclarationSelector<? extends NamespaceOrType> selector() {
    return new DeclarationSelector<NamespaceOrType>() {

      @Override
      public NamespaceOrType filter(Declaration declaration) throws LookupException {
        NamespaceOrType result;
        //@FIXME ugly hack with type enumeration
        if(((declaration instanceof Namespace) && (((Namespace)declaration).signature().getName().equals(getName())))
            || ((declaration instanceof Type) && (((Type)declaration).signature().getName().equals(getName())))){
        result = (NamespaceOrType) declaration;
        } else {
        result = null;
        }
        return result;
      }

      @Override
      public WeakPartialOrder<NamespaceOrType> order() {
        return new WeakPartialOrder<NamespaceOrType>() {

          @Override
          public boolean contains(NamespaceOrType first, NamespaceOrType second)
              throws LookupException {
            return first.equals(second);
          }
          
        };
      }

      @Override
      public Class<NamespaceOrType> selectedClass() {
        return NamespaceOrType.class;
      }
        
      };

  }
  
  /**
   * BAD DESIGN: YOU MUST OVERRIDE THIS IN A SUBCLASS
   */
 /*@
   @ also public behavior
   @
   @ post \fresh(\result);
   @ post \result.getName() == getName();
   @ post (* \result.getTarget() is a clone of getTarget() *);
   @*/
  public E clone() {
    return (E) new NamespaceOrTypeReference((getTarget() == null ? null : getTarget().clone()), getName());
  }

	public Declaration getElement() throws LookupException {
		return getNamespaceOrType();
	}

}
