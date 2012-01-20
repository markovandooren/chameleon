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
public class ConversionOperator<E extends ConversionOperator<E,H,S>, H extends SimpleNameMethodHeader<H,S>, S extends SimpleNameDeclarationWithParametersSignature> extends Operator<E,H,S> {

	  public ConversionOperator(TypeReference returnType) {
		    super((H)new SimpleNameMethodHeader("", returnType));
	  }

	  public boolean sameKind(Method other) {
		  return(other instanceof ConversionOperator);
	}

	  protected E cloneThis() {
	    return (E) new ConversionOperator((TypeReference)returnTypeReference().clone());
	  }

}
