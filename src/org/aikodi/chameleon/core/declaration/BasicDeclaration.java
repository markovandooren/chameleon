package org.aikodi.chameleon.core.declaration;

import org.aikodi.chameleon.core.event.name.NameChanged;
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

   private SimpleNameSignature _signature;

   @Override
   public void setName(String name) {
      if (_signature != null) {
        _name = name;
         _signature.setName(name);
         // In this case, the signature will send the event.
      } else {
        String old = _name;
        _name = name;
        if(changeNotificationEnabled()) {
          notify(new NameChanged(old, name));
        }
      }
   }

   @Override
   public void setSignature(Signature signature) {
      if (signature instanceof SimpleNameSignature) {
         setName(signature.name());
      } else {
         throw new ChameleonProgrammerException();
      }
   }

   @Override
   public SimpleNameSignature signature() {
      if (_signature == null) {
         synchronized (this) {
            if (_signature == null) {
               _signature = new SimpleNameSignature(_name) {
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

}
