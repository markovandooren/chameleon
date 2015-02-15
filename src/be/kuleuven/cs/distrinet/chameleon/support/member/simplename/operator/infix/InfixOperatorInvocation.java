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
    return getElement();
  }

  @Override
protected InfixOperatorInvocation cloneSelf() {
    return new InfixOperatorInvocation(name(), null);
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
  
//  @Override
//  public InfixOperator getElement() throws LookupException {
//  	Timer.INFIX_OPERATOR_INVOCATION.start();
//  	InfixOperator result = super.getElement();
//  	Timer.INFIX_OPERATOR_INVOCATION.stop();
//  	return result;
//  }

}
