package chameleon.support.member;

import chameleon.oo.member.SimpleNameDeclarationWithParametersSignature;
import chameleon.oo.method.Method;
import chameleon.oo.method.SimpleNameMethodHeader;
import chameleon.oo.type.TypeReference;
import chameleon.support.member.simplename.operator.Operator;

/**
 * @author Marko van Dooren
 * @author Tim Laeremans
 */
public class ConversionOperator extends Operator {

	  public ConversionOperator(TypeReference returnType) {
		    super(new SimpleNameMethodHeader("", returnType));
	  }

	  public boolean sameKind(Method other) {
		  return (other instanceof ConversionOperator);
	}

	  protected ConversionOperator cloneThis() {
	    return new ConversionOperator((TypeReference)returnTypeReference().clone());
	  }

}
