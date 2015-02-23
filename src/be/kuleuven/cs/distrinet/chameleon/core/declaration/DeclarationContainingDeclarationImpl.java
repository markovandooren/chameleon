package be.kuleuven.cs.distrinet.chameleon.core.declaration;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.ScopeProperty;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public abstract class DeclarationContainingDeclarationImpl extends DeclarationContainerImpl implements Declaration {

   public DeclarationContainingDeclarationImpl(Signature signature) {
      setSignature(signature);
   }
   
   @Override
   public String name() {
      return signature().name();
   }

   @Override
   public void setName(String name) {
      signature().setName(name);
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
   public Declaration selectionDeclaration() throws LookupException {
      return this;
   }

   @Override
   public Declaration actualDeclaration() throws LookupException {
      return this;
   }

   @Override
   public Declaration declarator() {
      return this;
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
   public Declaration finalDeclaration() {
      return this;
   }

   @Override
   public Declaration template() {
      return finalDeclaration();
   }

   @Override
   public SelectionResult updatedTo(Declaration declaration) {
      return declaration;
   }

   @Override
   public boolean sameSignatureAs(Declaration declaration) throws LookupException {
      return signature().sameAs(declaration.signature());
   }
   
   @Override
   public LookupContext targetContext() throws LookupException {
      return language().lookupFactory().createTargetLookupStrategy(this);
   }
}
