package org.aikodi.chameleon.core.declaration;

import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;

/**
 * A convenience class for implementation declaration containers.
 * 
 * @author Marko van Dooren
 */
public abstract class DeclarationContainerImpl extends ElementImpl implements DeclarationContainer {

   @Override
   public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector)
         throws LookupException {
      return selector.selection(declarations());
   }

   @Override
   public LookupContext localContext() throws LookupException {
      return language().lookupFactory().createLocalLookupStrategy(this);
   }

   @Override
   public LookupContext lookupContext(Element child) throws LookupException {
      return language().lookupFactory().createLexicalLookupStrategy(localContext(), this);
   }

   @Override
   public List<? extends Declaration> declarations() throws LookupException {
      return locallyDeclaredDeclarations();
   }
}
