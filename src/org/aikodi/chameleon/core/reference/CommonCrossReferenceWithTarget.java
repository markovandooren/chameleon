package org.aikodi.chameleon.core.reference;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupContext;
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

   protected LookupContext lookupContext() throws LookupException {
      CrossReferenceTarget target = getTarget();
      if (target != null) {
         return target.targetContext();
      } else {
         return lexicalContext();
      }

   }
}
