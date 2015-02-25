package org.aikodi.chameleon.support.member;

import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.method.SimpleNameMethodHeader;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.support.member.simplename.operator.Operator;

/**
 * @author Marko van Dooren
 * @author Tim Laeremans
 */
public class ConversionOperator extends Operator {

	  public ConversionOperator(TypeReference returnType) {
		    super(new SimpleNameMethodHeader("", returnType));
	  }

	  @Override
   public boolean sameKind(Method other) {
		  return (other instanceof ConversionOperator);
	}

	  @Override
   protected ConversionOperator cloneSelf() {
	    return new ConversionOperator(null);
	  }

}
