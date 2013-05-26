package be.kuleuven.cs.distrinet.chameleon.support.member.simplename.operator.infix;

import be.kuleuven.cs.distrinet.chameleon.oo.method.Method;
import be.kuleuven.cs.distrinet.chameleon.oo.method.MethodHeader;
import be.kuleuven.cs.distrinet.chameleon.support.member.simplename.operator.Operator;

/**
 * @author Marko van Dooren
 */
public class InfixOperator extends Operator {

  public InfixOperator(MethodHeader header) {
    super(header);
  }

  protected InfixOperator cloneSelf() {
    return new InfixOperator(null);
  }

  public boolean sameKind(Method other) {
	  return(other instanceof InfixOperator);
  }

}
