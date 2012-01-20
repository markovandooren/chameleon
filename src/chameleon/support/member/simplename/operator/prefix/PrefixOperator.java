package chameleon.support.member.simplename.operator.prefix;

import chameleon.oo.member.SimpleNameDeclarationWithParametersSignature;
import chameleon.oo.method.Method;
import chameleon.oo.method.SimpleNameMethodHeader;
import chameleon.support.member.simplename.operator.Operator;

public class PrefixOperator<E extends PrefixOperator<E,H,S>, H extends SimpleNameMethodHeader<H,S>, S extends SimpleNameDeclarationWithParametersSignature> extends Operator<E,H,S> {
  
  public PrefixOperator(H header) {
    super(header);
  }

  public boolean sameKind(Method other) {
  	return(other instanceof PrefixOperator);
  }

  protected E cloneThis() {
    return (E) new PrefixOperator(header().clone());
  }

}
