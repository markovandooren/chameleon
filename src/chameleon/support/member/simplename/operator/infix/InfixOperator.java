package chameleon.support.member.simplename.operator.infix;

import chameleon.oo.member.SimpleNameDeclarationWithParametersSignature;
import chameleon.oo.method.Method;
import chameleon.oo.method.SimpleNameMethodHeader;
import chameleon.support.member.simplename.operator.Operator;

/**
 * @author Marko van Dooren
 */
public class InfixOperator <E extends InfixOperator<E,H,S>, H extends SimpleNameMethodHeader<H,S>, S extends SimpleNameDeclarationWithParametersSignature> extends Operator<E,H,S> {

  public InfixOperator(H header) {
    super(header);
  }

  protected E cloneThis() {
    return (E) new InfixOperator(header().clone());
  }

  public boolean sameKind(Method other) {
	  return(other instanceof InfixOperator);
  }

}
