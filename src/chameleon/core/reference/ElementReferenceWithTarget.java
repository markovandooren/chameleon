package chameleon.core.reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.lookup.DeclarationCollector;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.util.Util;
import chameleon.util.association.Single;

public abstract class ElementReferenceWithTarget<R extends Declaration> extends ElementReference<R> implements CrossReferenceWithTarget<R>{

	/*@
	  @ public behavior
	  @
	  @ pre qn != null;
	  @
	  @ post getTarget() == getTarget(Util.getAllButLastPart(qn));
	  @ post getName() == Util.getLastPart(qn);
	  @*/
	 public ElementReferenceWithTarget(String qn) {
	   this(getTarget(Util.getAllButLastPart(qn)), new SimpleNameSignature(Util.getLastPart(qn)));
	 }
	 
	 protected static CrossReferenceTarget getTarget(String qn) {
		 if(qn == null) {
			 return null;
		 }
		 //ElementReference<? extends ElementReference<?,? extends TargetDeclaration>, ? extends TargetDeclaration> target = new SpecificReference<SpecificReferece,TargetDeclaration>(Util.getFirstPart(qn),TargetDeclaration.class);
		 SpecificReference<TargetDeclaration> target = new SpecificReference<TargetDeclaration>(Util.getFirstPart(qn),TargetDeclaration.class);
		 qn = Util.getSecondPart(qn);
		 while(qn != null) {
			 SpecificReference<TargetDeclaration> newTarget = new SpecificReference<TargetDeclaration>(Util.getFirstPart(qn),TargetDeclaration.class);
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
	 public ElementReferenceWithTarget(CrossReferenceTarget target, Signature signature) {
	 	super(signature);
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
		private Single<CrossReferenceTarget> _target = new Single<CrossReferenceTarget>(this);

		protected Single<CrossReferenceTarget> targetLink() {
			return _target;
		}
		
	 public CrossReferenceTarget getTarget() {
	   return _target.getOtherEnd();
	 }

	 public void setTarget(CrossReferenceTarget target) {
	   set(_target,target);
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
	   
		DeclarationCollector<X> collector = new DeclarationCollector<X>(selector);
	  CrossReferenceTarget targetReference = getTarget();
	  if(targetReference != null) {
//	  	TargetDeclaration<?,?> target = targetReference.getElement();
//	  	if(target != null) {
	  		targetReference.targetContext().lookUp(collector);
//	  	} else {
//	  		throw new LookupException("Lookup of target of NamespaceOrVariableReference returned null",targetReference);
//	  	}
	  }
	  else {
	  	lexicalLookupStrategy().lookUp(collector);
	  }
		result = collector.result();
//	  if(result != null) {
//	  	//OPTIMISATION
	  	if(cache) {
	  		setCache((R) result);
	  	}
	  	return result;
//	  } else {
//	  	// repeat lookups for debugging purposes
//	  	//Config.setCaching(false);
//	  	if(targetReference != null) {
//	  		result = targetReference.targetContext().lookUp(selector);
//	  	} else {
//	  		result = lexicalLookupStrategy().lookUp(selector);
//	  	}
//	  	throw new LookupException("Cannot find namespace or type with name: "+signature(),this);
//	  }
	 }

//	 public abstract DeclarationSelector<R> selector();
	 
	/*@
	  @ also public behavior
	  @
	  @ post \fresh(\result);
	  @ post \result.getName() == getName();
	  @ post (* \result.getTarget() is a clone of getTarget() *);
	  @*/
	 public abstract ElementReferenceWithTarget<R> clone() ;
	 
	 public String toString() {
		 return (getTarget() == null ? "" : getTarget().toString()+".")+signature().toString();
	 }
	 
}