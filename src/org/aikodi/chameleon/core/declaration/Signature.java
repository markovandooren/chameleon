package org.aikodi.chameleon.core.declaration;

import java.util.List;

import org.aikodi.chameleon.exception.ChameleonProgrammerException;

import com.google.common.collect.ImmutableList;

/**
 * A signature is a means of identifying a declaration that can be
 * cross-referenced. It contains only the information required for
 * identification. The return type of a method, for example, is not part of its
 * signature.
 * 
 * @author Marko van Dooren
 */
public abstract class Signature extends QualifiedName {

  /**
   * <p>
   * Return a string representation of the name of this signature. For methods,
   * e.g., the arguments are not included.
   * </p>
   * 
   * <p>
   * SPEED: This name is used to speed up selection of declarations in
   * declaration containers.
   * </p>
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public abstract String name();
  
  /**
   * Change the name of this signature to the given name.
   * 
   * @param name The new name of this signature. The name cannot be null.
   */
 /*@
   @ public behavior
   @
   @ post name().equals(name);
   @*/
  public abstract void setName(String name);
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Signature signatureAt(int index) {
    if (index != 1) {
      throw new ChameleonProgrammerException();
    } else {
      return this;
    }
  }

   /**
    * {@inheritDoc}
    * 
    * @return the name of this signature.
    */
   @Override
   public String toString() {
      return name();
   }
   
   /**
    * Check whether this signature has more properties than just the name.
    * This method is required to optimize name lookup without introducing
    * coupling to specific types.  
    * @return
    */
   public abstract boolean hasMorePropertiesThanName();

   /**
    * {@inheritDoc}
    */
   @Override
   public int length() {
      return 1;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Signature lastSignature() {
      return this;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<Signature> signatures() {
      return ImmutableList.of(this);
   }

}
