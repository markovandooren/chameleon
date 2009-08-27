package chameleon.core.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Marko van Dooren
 */
public abstract class Literal<E extends Literal> extends Expression<E> {
  
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
  public Set getDirectExceptions() {
    return new HashSet();
  }
  
 /*@
   @ also public behavior
   @
   @ post \result.isEmpty(); 
   @*/
  public List children() {
    return new ArrayList();
  }
}
