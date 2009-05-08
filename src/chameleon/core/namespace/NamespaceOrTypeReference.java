/*
 * Copyright 2000-2004 the Jnome development team.
 *
 * @author Marko van Dooren
 * @author Nele Smeets
 * @author Kristof Mertens
 * @author Jan Dockx
 *
 * This file is part of Jnome.
 *
 * Jnome is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * Jnome is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Jnome; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 */
package chameleon.core.namespace;

import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationSelector;
import chameleon.core.reference.ElementReference;
import chameleon.core.relation.WeakPartialOrder;
import chameleon.core.type.Type;
import chameleon.util.Util;

/**
 * Generic Parameter R is the type of the referenced element.
 * @author marko
 */
public class NamespaceOrTypeReference<E extends NamespaceOrTypeReference, R extends NamespaceOrType> extends ElementReference<E,R> {
  
 /*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post getTarget() == null;
   @ post getName() == name;
   @*/
  public NamespaceOrTypeReference(String name) {
    this(null,  name);
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
	 * TARGET
	 */
	private Reference<NamespaceOrTypeReference,NamespaceOrTypeReference> _target = new Reference<NamespaceOrTypeReference,NamespaceOrTypeReference>(this);

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
  public NamespaceOrType getNamespaceOrType() throws MetamodelException {
    NamespaceOrType result = null;
    
    //OPTIMISATION
    result = getCache();
    if(result != null) {
    	return result;
    }
    
    if(getTarget() != null) {
    	NamespaceOrType target = getTarget().getNamespaceOrType();
      
      if(target != null) {
        result = target.targetContext().lookUp(selector());//findNamespaceOrType(getName());
      }
    }
    else {
      result = lexicalContext().lookUp(selector());//findNamespaceOrType(getName());
    }
    if(result != null) {
    	//OPTIMISATION
    	setCache((R) result);
      return result;
    } else {
      lexicalContext().lookUp(selector());//findNamespaceOrType(getName());
      throw new MetamodelException("Cannot find namespace or type with name: "+getName());
    }
  }
  
  public DeclarationSelector<? extends NamespaceOrType> selector() {
    return new DeclarationSelector<NamespaceOrType>() {

      @Override
      public NamespaceOrType filter(Declaration declaration) throws MetamodelException {
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
              throws MetamodelException {
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
    return (E) new NamespaceOrTypeReference(getTarget().clone(), getName());
  }

	public Declaration getElement() throws MetamodelException {
		return getNamespaceOrType();
	}

}
