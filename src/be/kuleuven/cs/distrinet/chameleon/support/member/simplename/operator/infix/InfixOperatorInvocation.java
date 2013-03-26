package be.kuleuven.cs.distrinet.chameleon.support.member.simplename.operator.infix;


import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReferenceTarget;
import be.kuleuven.cs.distrinet.chameleon.support.member.simplename.SimpleNameMethodInvocation;


/**
 * @author Marko van Dooren
 */
public class InfixOperatorInvocation extends SimpleNameMethodInvocation<InfixOperator> {

  /**
   * @param target
   * @param name
   */
  public InfixOperatorInvocation(String name, CrossReferenceTarget target) {
    super(target, name);
  }

  public InfixOperator getInfixOperator() throws LookupException {
    return (InfixOperator)getElement();
  }

  protected InfixOperatorInvocation cloneInvocation(CrossReferenceTarget target) {
    return new InfixOperatorInvocation(name(), target);
  }

  @Override
  public DeclarationSelector<InfixOperator> createSelector() {
    return new SimpleNameMethodSelector() {
      @Override
      public Class<InfixOperator> selectedClass() {
        return InfixOperator.class;
      }

    };
  }

}
