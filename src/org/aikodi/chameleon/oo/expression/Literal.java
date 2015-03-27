package org.aikodi.chameleon.oo.expression;

/**
 * A class of literals
 * 
 * @author Marko van Dooren
 */
public abstract class Literal extends Expression {

  /**
   * Create a new literal with the given text.
   * 
   * @param text The text of the literal.
   */
  public Literal(String text) {
    if(text == null) {
      throw new IllegalArgumentException("The value of a literal cannot be null.");
    }
    _text = text;
  }
  
  /**
   * VALUE
   */
  private final String _text;
  
  public String text() {
    return _text;
  }
}
