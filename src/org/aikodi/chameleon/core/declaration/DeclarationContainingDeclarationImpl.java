package org.aikodi.chameleon.core.declaration;

import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.modifier.ElementWithModifiersImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.core.scope.ScopeProperty;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.util.association.Single;

public abstract class DeclarationContainingDeclarationImpl extends ElementWithModifiersImpl implements Declaration, DeclarationContainer {

   public DeclarationContainingDeclarationImpl(Signature signature) {
      setSignature(signature);
   }
   
   private Single<Signature> _signature = new Single<Signature>(this);

   @Override
   public Signature signature() {
      return _signature.getOtherEnd();
   }

   @Override
   public void setSignature(Signature signature) {
      set(_signature, signature);
   }

   @Override
   public Scope scope() throws ModelException {
      Scope result = null;
      ChameleonProperty scopeProperty = property(language().SCOPE_MUTEX());
      if (scopeProperty instanceof ScopeProperty) {
         result = ((ScopeProperty) scopeProperty).scope(this);
      } else if (scopeProperty != null) {
         throw new ChameleonProgrammerException("Scope property is not a ScopeProperty");
      }
      return result;
   }

   @Override
   public boolean complete() throws LookupException {
      return true;
   }

   @Override
   public LookupContext targetContext() throws LookupException {
      return language().lookupFactory().createTargetLookupStrategy(this);
   }
}
