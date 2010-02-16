package chameleon.core.namespace;

import chameleon.core.declaration.Signature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.expression.NamedTarget;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;
import chameleon.core.reference.SpecificReference;

/**
 * Generic Parameter R is the type of the referenced element.
 * @author Marko van Dooren
 */
public class NamespaceOrTypeReference extends SpecificReference<NamespaceOrTypeReference,Element,NamespaceOrType> {
  
 /*@
   @ public behavior
   @
   @ pre qn != null;
   @
   @ post getTarget() == getTarget(Util.getAllButLastPart(qn));
   @ post getName() == Util.getLastPart(qn);
   @*/
  public NamespaceOrTypeReference(String qn) {
    super(qn,NamespaceOrType.class);
  }
  
 /*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post getTarget() == target;
   @ post getName() == name;
   @*/
  public NamespaceOrTypeReference(CrossReference<? , ?, ? extends TargetDeclaration> target, String name) {
  	super(target,name,NamespaceOrType.class);
  }
  
 /*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post getTarget() == target;
   @ post getName() == name;
   @*/
  public NamespaceOrTypeReference(CrossReference<? , ?, ? extends TargetDeclaration> target, Signature signature) {
 	  super(target,signature,NamespaceOrType.class);
  }

 public NamespaceOrTypeReference(NamedTarget target) {
  	this(target.getTarget() == null ? null : new NamespaceOrTypeReference((NamedTarget)target.getTarget()),target.getName());
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
//  public String getFullyQualifiedName() {
//  	if(getTarget() == null) {
//  		return getName();
//  	} else {
//  		return getTarget().getFullyQualifiedName()+"."+getName();
//  	}
//  }
  
  
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
  	return getElement();
  }
  
  
 /*@
   @ also public behavior
   @
   @ post \fresh(\result);
   @ post \result.getName() == getName();
   @ post (* \result.getTarget() is a clone of getTarget() *);
   @*/
  public NamespaceOrTypeReference clone() {
    return new NamespaceOrTypeReference((getTarget() == null ? null : getTarget().clone()), signature().clone());
  }

}
