package org.aikodi.chameleon.core.reference;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.DeclarationCollector;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.util.association.Single;

public abstract class CommonCrossReferenceWithTarget<D extends Declaration> extends CrossReferenceImpl<D> implements CrossReferenceWithTarget<D> {

   public CommonCrossReferenceWithTarget(CrossReferenceTarget target) {
      setTarget(target);
   }
   
   /**
    * TARGET
    */
   private Single<CrossReferenceTarget> _target = new Single<CrossReferenceTarget>(this);

   protected Single<CrossReferenceTarget> targetLink() {
      return _target;
   }

   @Override
   public CrossReferenceTarget getTarget() {
      return _target.getOtherEnd();
   }

   @Override
   public void setTarget(CrossReferenceTarget target) {
      set(_target,target);
   }

   @Override
   protected <X extends Declaration> X getElement(DeclarationSelector<X> selector) throws LookupException {
      X result = null;

      //OPTIMISATION
      boolean cache = selector.equals(selector());
      if(cache) {
            result = (X) getCache();
      }
      if(result != null) {
         return result;
      }
      synchronized (this) {
         DeclarationCollector<X> collector = new DeclarationCollector<X>(selector);
         CrossReferenceTarget target = getTarget();
         if (target != null) {
            target.targetContext().lookUp(collector);
         } else {
            lexicalContext().lookUp(collector);
         }
         result = collector.result();
         if (cache) {
            setCache((D) result);
         }
         return result;
      }
   }


   
}
