package be.kuleuven.cs.distrinet.chameleon.support.member.simplename.operator.infix;

import be.kuleuven.cs.distrinet.chameleon.oo.method.Method;
import be.kuleuven.cs.distrinet.chameleon.oo.method.MethodHeader;
import be.kuleuven.cs.distrinet.chameleon.support.member.simplename.operator.Operator;
import be.kuleuven.cs.distrinet.chameleon.util.CreationStackTrace;

/**
 * @author Marko van Dooren
 */
public class InfixOperator extends Operator {

  public InfixOperator(MethodHeader header) {
    super(header);
  }

  protected InfixOperator cloneThis() {
    return new InfixOperator((MethodHeader) header().clone());
  }

  public boolean sameKind(Method other) {
	  return(other instanceof InfixOperator);
  }

}
