package chameleon.core.declaration;

import chameleon.exception.ChameleonProgrammerException;

/**
 * A signature is a means of identifying a declaration that can be cross-referenced. It contains
 * only the information required for identification. The return type of a method, for example,
 * is not part of its signature.
 * 
 * @author Marko van Dooren
 */
public abstract class Signature<E extends Signature<E>> extends QualifiedName<E> {

  public abstract E clone();
  
  /**
   * Return a string representation of the name of this signature. For methods, e.g., the arguments
   * are not included.
   * 
   * SPEED: This name is used to speed up selection of declarations in declaration containers. 
   */
  public abstract String name();
  
  /**
   * Change the name of this signature to the given name.
   */
 /*@
   @ public behavior
   @
   @ post name().equals(name);
   @*/
  public abstract void setName(String name);
  
  /* (non-Javadoc)
   * @see chameleon.core.declaration.QualifiedName#signatureAt(int)
   */
  public Signature signatureAt(int index) {
  	if(index != 1) {
  		throw new ChameleonProgrammerException();
  	} else {
  		return this;
  	}
  }
  
  /**
   * The default string representation of a signature is its name.
   */
  public String toString() {
  	return name();
  }
}
