package be.kuleuven.cs.distrinet.chameleon.support.expression;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.TargetDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReferenceTarget;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.NamedTarget;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.CheckedExceptionList;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.inheritance.InheritanceRelation;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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

  public SuperTarget cloneSelf() {
    return new SuperTarget();
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

  public LookupContext targetContext() throws LookupException {
    return getTargetDeclaration().targetContext();
  }

  /**
   * A super target is always valid. If invocations on a super target must always resolve to an effective declaration, 
   * as is the case in Java, then the language must add that rule. For mixins, for example, that must only be the case for
   * an actual combination of mixins.
   */
	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

}
