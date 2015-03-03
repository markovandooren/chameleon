package org.aikodi.chameleon.core.declaration;

import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;

/**
 * An abstract class for signatures that have a name.
 * 
 * @author Marko van Dooren
 */
public abstract class SignatureWithName extends Signature {

   /**
    * The name of the signature.
    */
   private String _name;

   /**
    * Create a new signture with the given name.
    * 
    * @param name The name of the new signature.
    */
  /*@
    @ public behavior
    @
    @ pre name != null;
    @
    @ post name().equals(name);
    @*/
   public SignatureWithName(String name) {
      setName(name);
   }

   @Override
   public String name() {
      return _name;
   }

   @Override
   public void setName(String name) {
      if(name == null) {
         throw new IllegalArgumentException("The name of a signature with a name cannot be null.");
      }
      _name = name;
   }

   /**
    * {@inheritDoc}
    * 
    * @return the {@link Object#hashCode()} of the {@link #name()}.
    */
   @Override
   public int hashCode() {
      return _name.hashCode();
   }

   @Override
   public Verification verifySelf() {
      if (_name == null) {
         return new SignatureWithoutName(this);
      } else {
         return Valid.create();
      }
   }

}