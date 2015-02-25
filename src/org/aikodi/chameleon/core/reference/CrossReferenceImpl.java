package org.aikodi.chameleon.core.reference;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.DeclaratorSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;

/**
 * An class with default implementations for cross-references.
 * 
 * @author Marko van Dooren
 *
 * @param <D> The type of the declaration that is referenced by the cross-reference.
 */
public abstract class CrossReferenceImpl<D extends Declaration> extends ElementImpl implements CrossReference<D> {

   /**
    * Return the declaration selector that is responsible for selecting the
    * declaration referenced by this cross-reference.
    * 
    * @return
    */
   public abstract DeclarationSelector<D> selector();

   @Override
   public D getElement() throws LookupException {
      return getElement(selector());
   }

   @Override
   public Declaration getDeclarator() throws LookupException {
      return getElement(new DeclaratorSelector(selector()));
   }

   protected abstract <X extends Declaration> X getElement(DeclarationSelector<X> selector) throws LookupException;

   @Override
   public Verification verifySelf() {
      try {
         return getElement() != null ? Valid.create() : new UnresolvableCrossReference(this);
      } catch (LookupException e) {
         return new UnresolvableCrossReference(this, e.getMessage());
      } catch (ChameleonProgrammerException e) {
         return new UnresolvableCrossReference(this, e.getMessage());
      }
   }

   @Override
   public LookupContext targetContext() throws LookupException {
      return getElement().targetContext();
   }

}
