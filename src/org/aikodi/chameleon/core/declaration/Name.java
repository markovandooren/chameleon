package org.aikodi.chameleon.core.declaration;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;

/**
 * A class of signatures that consist of just a name.
 * 
 * @author Marko van Dooren
 */
public class Name extends SignatureWithName {

   /**
    * Create a new name signature with the given name.
    * 
    * @param name The name of the signature.
    */
  /*@
    @ public behavior
    @
    @ post name().equals(name);
    @*/
   public Name(String name) {
      super(name);
   }

   /**
    * {@inheritDoc}
    * 
    * A name is equal to another element if the latter is a Name
    * with the same {@link #name()}.
    */
   @Override
   public boolean uniSameAs(Element other) throws LookupException {
      boolean result = false;
      if (other instanceof Name) {
         Name sig = (Name) other;
         String name = name();
         result = name != null && name.equals(sig.name());
      }
      return result;
   }

   @Override
   public Name cloneSelf() {
      return new Name(name());
   }

   /**
    * {@inheritDoc}
    * 
    * A name signature does not have more properties than a name.
    */
  /*@
    @ public behavior
    @
    @ post \result == false;
    @*/
   @Override
   public boolean hasMorePropertiesThanName() {
      return false;
   }

}
