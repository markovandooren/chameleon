package org.aikodi.chameleon.core.declaration;

import org.aikodi.chameleon.exception.ChameleonProgrammerException;

/**
 * A class provides default implementations for declarations.
 * 
 * @author Marko van Dooren
 */
public abstract class BasicDeclaration extends DeclarationImpl {

   public BasicDeclaration(String name) {
      setName(name);
   }

   private String _name;

   @Override
   public String name() {
      return _name;
   }

   private Name _signature;

   @Override
   public void setName(String name) {
      _name = name;
      if (_signature != null) {
         _signature.setName(name);
      }
   }

   @Override
   public void setSignature(Signature signature) {
      if (signature instanceof Name) {
         setName(signature.name());
      } else {
         throw new ChameleonProgrammerException();
      }
   }

   @Override
   public Name signature() {
      if (_signature == null) {
         synchronized (this) {
            if (_signature == null) {
               _signature = new Name(_name) {
                  @Override
                  public void setName(String name) {
                     super.setName(name);
                     BasicDeclaration.this._name = name;
                  }
               };
            }
            _signature.setUniParent(this);
         }
      }
      return _signature;
   }

//   @Override
//   public boolean sameSignatureAs(Declaration declaration) {
////      if (declaration instanceof BasicDeclaration) {
////         return _name.equals(((BasicDeclaration) declaration)._name);
////      } else {
////         return declaration.name().equals(_name) && declaration instanceof SimpleNameDeclaration;
////      }
//      return signature().sameAs(declaration.signature());
//   }

}
