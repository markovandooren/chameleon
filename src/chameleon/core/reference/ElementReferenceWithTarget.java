package chameleon.core.reference;

import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.Config;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.util.Util;

public abstract class ElementReferenceWithTarget<E extends ElementReferenceWithTarget, P extends Element, R extends Declaration> extends ElementReference<E,P,R> {

	/*@
	  @ public behavior
	  @
	  @ pre qn != null;
	  @
	  @ post getTarget() == getTarget(Util.getAllButLastPart(qn));
	  @ post getName() == Util.getLastPart(qn);
	  @*/
	 public ElementReferenceWithTarget(String qn) {
	   this(getTarget(Util.getAllButLastPart(qn)), Util.getLastPart(qn));
	 }
	 
   protected static ElementReference<? extends ElementReference<?,?,? extends TargetDeclaration>,?, ? extends TargetDeclaration> getTarget(String qn) {
     if(qn == null) {
       return null;
     }
     //ElementReference<? extends ElementReference<?,? extends TargetDeclaration>, ? extends TargetDeclaration> target = new SpecificReference<SpecificReferece,TargetDeclaration>(Util.getFirstPart(qn),TargetDeclaration.class);
     SpecificReference<SpecificReference<SpecificReference,Element,TargetDeclaration>, Element,TargetDeclaration> target = new SpecificReference<SpecificReference<SpecificReference,Element,TargetDeclaration>,Element,TargetDeclaration>(Util.getFirstPart(qn),TargetDeclaration.class);
     qn = Util.getSecondPart(qn);
     while(qn != null) {
       SpecificReference<SpecificReference<SpecificReference,Element,TargetDeclaration>,Element,TargetDeclaration> newTarget = new SpecificReference<SpecificReference<SpecificReference,Element,TargetDeclaration>,Element,TargetDeclaration>(Util.getFirstPart(qn),TargetDeclaration.class);
       newTarget.setTarget(target);
       target = newTarget;
       qn = Util.getSecondPart(qn);
     }
   return target;
}

	/*@
	  @ public behavior
	  @
	  @ pre name != null;
	  @
	  @ post getTarget() == target;
	  @ post getName() == name;
	  @*/
	 public ElementReferenceWithTarget(ElementReference<?,?,? extends TargetDeclaration> target, String name) {
	 	super(name);
		  setTarget((ElementReference<? extends ElementReference<?, ? super ElementReferenceWithTarget, ? extends TargetDeclaration>, ? super ElementReferenceWithTarget,? extends TargetDeclaration>) target); 
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
/*	 public String getFullyQualifiedName() {
	 	if(getTarget() == null) {
	 		return getName();
	 	} else {
	 		return getTarget().getFullyQualifiedName()+"."+getName();
	 	}
	 } */

		/**
		 * TARGET
		 */
		private Reference<ElementReferenceWithTarget,ElementReference<?, ?, ? extends TargetDeclaration>> _target = new Reference<ElementReferenceWithTarget,ElementReference<?, ?, ? extends TargetDeclaration>>(this);

		protected Reference<ElementReferenceWithTarget,ElementReference<?, ?, ? extends TargetDeclaration>> targetLink() {
			return _target;
		}
		
	 public ElementReference<?, ?, ? extends TargetDeclaration> getTarget() {
	   return _target.getOtherEnd();
	 }

	 public void setTarget(ElementReference<? extends ElementReference<?,? super ElementReferenceWithTarget,? extends TargetDeclaration>, ? super ElementReferenceWithTarget, ? extends TargetDeclaration> target) {
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
	 public List<Element> children() {
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
	 public <X extends Declaration> X getElement(DeclarationSelector<X> selector) throws LookupException {
	   X result = null;
	   
	   //OPTIMISATION
	   boolean cache = selector.equals(selector());
	   if(cache) {
	     result = (X) getCache();
	   }
	   if(result != null) {
	   	return result;
	   }
	   
	   if(getTarget() != null) {
	   	TargetDeclaration target = getTarget().getElement();
	     
	     if(target != null) {
	       result = target.targetContext().lookUp(selector);
	     } else {
	     	throw new LookupException("Lookup of target of NamespaceOrVariableReference returned null",getTarget());
	     }
	   }
	   else {
	     result = lexicalLookupStrategy().lookUp(selector);
	   }
	   if(result != null) {
	   	//OPTIMISATION
	  	 if(cache) {
	   	   setCache((R) result);
	  	 }
	     return result;
	   } else {
	   	// repeat lookups for debugging purposes
	   	Config.CACHE_ELEMENT_REFERENCES = false;
	   	if(getTarget() != null) {
	     	TargetDeclaration target = getTarget().getElement();
	       
	       if(target != null) {
	         result = target.targetContext().lookUp(selector);
	       }
	   	} else {
	   		result = lexicalLookupStrategy().lookUp(selector);
	   	}
	     throw new LookupException("Cannot find namespace or type with name: "+getName(),this);
	   }
	 }
	 
	 public abstract DeclarationSelector<R> selector();
	 
	/*@
	  @ also public behavior
	  @
	  @ post \fresh(\result);
	  @ post \result.getName() == getName();
	  @ post (* \result.getTarget() is a clone of getTarget() *);
	  @*/
	 public abstract E clone() ;
	 
}