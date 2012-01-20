package chameleon.support.member.simplename.operator.postfix;

import chameleon.oo.member.SimpleNameDeclarationWithParametersSignature;
import chameleon.oo.method.Method;
import chameleon.oo.method.SimpleNameMethodHeader;
import chameleon.support.member.simplename.operator.Operator;

/**
 * @author Marko van Dooren
 */
public class PostfixOperator<E extends PostfixOperator<E,H,S>, H extends SimpleNameMethodHeader<H,S>, S extends SimpleNameDeclarationWithParametersSignature> extends Operator<E,H,S> {

  public PostfixOperator(H header) {
    super(header);
  }
  
  public boolean sameKind(Method other) {
	  	return(other instanceof PostfixOperator);
	  }  

  protected E cloneThis() {
    return (E) new PostfixOperator(header().clone());
  }

}
