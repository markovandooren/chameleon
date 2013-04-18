package be.kuleuven.cs.distrinet.chameleon.support.member;

import be.kuleuven.cs.distrinet.chameleon.oo.method.Method;
import be.kuleuven.cs.distrinet.chameleon.oo.method.SimpleNameMethodHeader;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.support.member.simplename.operator.Operator;

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
