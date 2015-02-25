package org.aikodi.chameleon.core.declaration;

import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.util.Util;

/**
 * A class of signatures that consist of just a name.
 * 
 * @author Marko van Dooren
 */
public class SimpleNameSignature extends Signature {

   /**
    * The name of the signature.
    */
   private String _name;

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
   public SimpleNameSignature(String name) {
      setName(name);
   }

   @Override
   public String name() {
      return _name;
   }

   @Override
   public void setName(String name) {
      _name = name;
   }

   @Override
   public boolean uniSameAs(Element other) throws LookupException {
      boolean result = false;
      if (other instanceof SimpleNameSignature) {
         SimpleNameSignature sig = (SimpleNameSignature) other;
         String name = name();
         result = name != null && name.equals(sig.name());
      }
      return result;
   }

   @Override
   public int hashCode() {
      return _name.hashCode();
   }

   @Override
   public SimpleNameSignature cloneSelf() {
      return new SimpleNameSignature(name());
   }

   @Override
   public Verification verifySelf() {
      if (_name == null) {
         return new SignatureWithoutName(this);
      } else {
         return Valid.create();
      }
   }

   @Override
   public String toString() {
      return _name;
   }

   @Override
   public Signature lastSignature() {
      return this;
   }

   @Override
   public List<Signature> signatures() {
      return Util.createSingletonList((Signature) this);
   }

   @Override
   public int length() {
      return 1;
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