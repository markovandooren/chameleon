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
public abstract class BasicDeclaration extends ElementImpl implements Declaration {

	/**
	 * Create a new declaration with the given name.
	 * 
	 * @param name The name of the declaration.
	 */
   public BasicDeclaration(String name) {
      setSignature(new SimpleNameSignature(name));
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

   
   /*************
    * MODIFIERS *
    *************/
   private Multi<Modifier> _modifiers = new Multi<Modifier>(this);

   /**
    * @return the list of modifiers of this member.
    */
   /*@
    @ behavior
    @
    @ post \result != null;
    @*/
   @Override
   public List<Modifier> modifiers() {
     return _modifiers.getOtherEnds();
   }

   /**
    * Add the given modifier to this element.
    * 
    * @param modifier The modifier to be added.
    */
   /*@
    @ public behavior
    @
    @ pre modifier != null;
    @
    @ post modifiers().contains(modifier);
    @*/
   @Override
   public void addModifier(Modifier modifier) {
     add(_modifiers, modifier);
   }

   /**
    * Remove the given modifier from this element.
    * 
    * @param modifier The modifier that must be removed.
    */
   /*@
    @ public behavior
    @
    @ pre modifier != null;
    @
    @ post ! modifiers().contains(modifier);
    @*/
   @Override
   public void removeModifier(Modifier modifier) {
     remove(_modifiers, modifier);
   }

   /**
    * Check whether this element contains the given modifier.
    * 
    * @param modifier The modifier that is searched.
    */
   /*@
    @ public behavior
    @
    @ pre modifier != null;
    @
    @ post \result == modifiers().contains(modifier);
    @*/
   public boolean hasModifier(Modifier modifier) {
     return _modifiers.getOtherEnds().contains(modifier);
   }

}
