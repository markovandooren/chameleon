package org.aikodi.chameleon.support.member.simplename.operator.prefix;


import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.support.member.simplename.SimpleNameMethodInvocation;

/**
 * @author Marko van Dooren
 */
public class PrefixOperatorInvocation extends SimpleNameMethodInvocation<PrefixOperator> {

  /**
   * @param target
   * @param name
   */
  public PrefixOperatorInvocation(String name, CrossReferenceTarget target) {
    super(target, name);
  }

  @Override
protected PrefixOperatorInvocation cloneSelf() {
    return new PrefixOperatorInvocation(name(), null);
  }

  @Override
  public DeclarationSelector<PrefixOperator> createSelector() {
    return new SimpleNameMethodSelector() {
      @Override
      public Class<PrefixOperator> selectedClass() {
        return PrefixOperator.class;
      }

    };
  }
  
  /**
   * @{inheritDoc}
   */
  @Override
  public Class<PrefixOperator> referencedType() {
    return PrefixOperator.class;
  }

}
