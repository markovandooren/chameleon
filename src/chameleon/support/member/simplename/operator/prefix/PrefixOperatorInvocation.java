package chameleon.support.member.simplename.operator.prefix;


import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.reference.CrossReferenceTarget;
import chameleon.support.member.simplename.SimpleNameMethodInvocation;

/**
 * @author Marko van Dooren
 */
public class PrefixOperatorInvocation extends SimpleNameMethodInvocation<PrefixOperatorInvocation,PrefixOperator> {

  /**
   * @param target
   * @param name
   */
  public PrefixOperatorInvocation(String name, CrossReferenceTarget target) {
    super(target, name);
  }

  protected PrefixOperatorInvocation cloneInvocation(CrossReferenceTarget target) {
    return new PrefixOperatorInvocation(name(), target);
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
  
}
