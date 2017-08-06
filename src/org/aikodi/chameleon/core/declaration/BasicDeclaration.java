package org.aikodi.chameleon.core.declaration;

import java.util.List;

import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.modifier.ElementWithModifiers;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.chameleon.util.association.Single;

/**
 * A class that provides default implementations for declarations.
 * 
 * @author Marko van Dooren
 */
public abstract class BasicDeclaration extends DeclarationImpl implements Declaration {

	/**
	 * Create a new declaration with the given name.
	 * 
	 * @param name The name of the declaration.
	 */
   public BasicDeclaration(String name) {
      setSignature(new Name(name));
   }
   
   public BasicDeclaration(Signature signature) {
     setSignature(signature);
   }
   
   protected BasicDeclaration() {
     
   }
   
   private Single<Signature> _signature = createSignatureLink();

   protected Single<Signature> createSignatureLink() {
     return new Single<Signature>(this);
   }
   
   @Override
   public void setSignature(Signature signature) {
      set(_signature, signature);
   }
   
   @Override
  public Signature signature() {
    return _signature.getOtherEnd();
  }

   
}
