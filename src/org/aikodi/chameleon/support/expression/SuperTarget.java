package org.aikodi.chameleon.support.expression;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.NamedTarget;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.inheritance.InheritanceRelation;
import org.aikodi.chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public class SuperTarget extends ElementImpl implements CrossReference<Declaration> {

  public SuperTarget() {
  }

  public SuperTarget(CrossReferenceTarget target) {
    setTarget(target);
  }
  
  /**
   * @{inheritDoc}
   */
  @Override
  public Class<Declaration> referencedType() {
    return Declaration.class;
  }

  /**
   * TARGET
   */
  private Single<CrossReferenceTarget> _target = new Single<CrossReferenceTarget>(this, false, "target");

  public CrossReferenceTarget getTarget() {
    return _target.getOtherEnd();
  }

  public void setTarget(CrossReferenceTarget target) {
    set(_target, target);
  }

  @Override
  public SuperTarget cloneSelf() {
    return new SuperTarget();
  }

  @Override
  public Declaration getElement() throws LookupException {
    if (getTarget() != null) {
      return ((NamedTarget) getTarget()).getElement();
    } else {
      Type outer = nearestAncestor(Type.class);
      List<InheritanceRelation> inh = outer.inheritanceRelations();
      Type result = (Type) inh.get(0).target();
      return result;
    }
  }

  /**
   * A super target is always valid. If invocations on a super target must
   * always resolve to an effective declaration, as is the case in Java, then
   * the language must add that rule. For mixins, for example, that must only be
   * the case for an actual combination of mixins.
   */
  @Override
  public Verification verifySelf() {
    return Valid.create();
  }

}
