package chameleon.support.expression;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LocalLookupStrategy;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReferenceTarget;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.NamedTarget;
import chameleon.oo.statement.CheckedExceptionList;
import chameleon.oo.type.Type;
import chameleon.oo.type.inheritance.InheritanceRelation;
import chameleon.util.Util;

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
	private SingleAssociation<CrossReferenceTarget,CrossReferenceTarget> _target = new SingleAssociation<CrossReferenceTarget,CrossReferenceTarget>(this);

  public CrossReferenceTarget getTarget() {
    return _target.getOtherEnd();
  }

  public void setTarget(CrossReferenceTarget target) {
    setAsParent(_target,target);
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

  public LocalLookupStrategy targetContext() throws LookupException {
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
