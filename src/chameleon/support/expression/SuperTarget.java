package chameleon.support.expression;

import java.util.List;

import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.reference.CrossReferenceTarget;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.NamedTarget;
import chameleon.oo.statement.CheckedExceptionList;
import chameleon.oo.type.Type;
import chameleon.oo.type.inheritance.InheritanceRelation;
import chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public class SuperTarget extends ElementImpl implements CrossReferenceTarget {

  public SuperTarget() {
	}
  
  public SuperTarget(CrossReferenceTarget target) {
  	setTarget(target);
  }

	/**
	 * TARGET
	 */
	private Single<CrossReferenceTarget> _target = new Single<CrossReferenceTarget>(this,false);

  public CrossReferenceTarget getTarget() {
    return _target.getOtherEnd();
  }

  public void setTarget(CrossReferenceTarget target) {
    set(_target,target);
  }

  public SuperTarget clone() {
    SuperTarget result = new SuperTarget();
    if(getTarget() != null) {
      result.setTarget(getTarget().clone());
    }
    return result;
  }

  public CheckedExceptionList getCEL() {
    return new CheckedExceptionList();
  }

  public CheckedExceptionList getAbsCEL() {
    return new CheckedExceptionList();
  }

  public TargetDeclaration getTargetDeclaration() throws LookupException {
    if(getTarget() != null) {
      return ((NamedTarget)getTarget()).getElement();
    } else {
      Type outer = nearestAncestor(Type.class);
      List<InheritanceRelation> inh = outer.inheritanceRelations();
      Type result = (Type) inh.get(0).superElement();
			return result;
    }
  }

  public LookupStrategy targetContext() throws LookupException {
    return getTargetDeclaration().targetContext();
  }

  /**
   * A super target is always valid. If invocations on a super target must always resolve to an effective declaration, 
   * as is the case in Java, then the language must add that rule. For mixins, for example, that must only be the case for
   * an actual combination of mixins.
   */
	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

}
