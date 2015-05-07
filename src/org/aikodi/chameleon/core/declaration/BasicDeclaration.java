package org.aikodi.chameleon.core.declaration;

import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.util.association.Single;

/**
 * A class that provides default implementations for declarations.
 * 
 * @author Marko van Dooren
 */
public abstract class BasicDeclaration extends DeclarationImpl {

   public BasicDeclaration(String name) {
      setSignature(new SimpleNameSignature(name));
   }
   
   protected BasicDeclaration() {
     
   }

//   private String _name;

//   @Override
//   public String name() {
//      return _name;
//   }

   private Single<Signature> _signature = new Single<>(this);

//   @Override
//   public void setName(String name) {
//  	 if(name == null) {
//  		 throw new IllegalArgumentException("The name of a declaration cannot be null. Only the signature can be null.");
//  	 }
//      if (_signature != null) {
//        _name = name;
//         _signature.setName(name);
//         // In this case, the signature will send the event.
//      } else {
//        String old = _name;
//        _name = name;
//        if(changeNotificationEnabled()) {
//          notify(new NameChanged(old, name));
//        }
//      }
//   }

   @Override
   public void setSignature(Signature signature) {
      set(_signature, signature);
   }
   
   @Override
  public Signature signature() {
    return _signature.getOtherEnd();
  }

//   @Override
//   public SimpleNameSignature signature() {
//      if (_signature.getOtherEnd() == null) {
//         synchronized (this) {
//            if (_signature == null) {
//               set(_signature,new SimpleNameSignature(_name) {
//                  @Override
//                  public void setName(String name) {
//                     super.setName(name);
//                     BasicDeclaration.this._name = name;
//                  }
//               });
//            }
//            _signature.setUniParent(this);
//         }
//      }
//      return _signature;
//   }

}
