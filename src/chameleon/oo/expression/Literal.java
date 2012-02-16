package chameleon.oo.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chameleon.core.element.Element;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.Type;


/**
 * @author Marko van Dooren
 */
public abstract class Literal extends Expression {
  
  public Literal(String value) {
    setValue(value);
  }
  
  /**
   * VALUE
   */
  
  private String _value;
  
  public String getValue() {
    return _value;
  }

  public void setValue(String value) {
  	_value = value;
  }
  
  /*@
    @ also public behavior
    @
    @ post \result.isEmpty(); 
    @*/
  public Set<Type> getDirectExceptions() {
    return new HashSet<Type>();
  }
  
 /*@
   @ also public behavior
   @
   @ post \result.isEmpty(); 
   @*/
  public List<Element> children() {
    return new ArrayList();
  }
  
	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
		if(getValue() == null) {
			result = result.and(new BasicProblem(this, "The value of the literal is missing."));
		}
		return result;
	}

}
