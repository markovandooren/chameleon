package org.aikodi.chameleon.oo.type;

import java.util.List;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.modifier.ElementWithModifiersImpl;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.member.Member;
import org.aikodi.chameleon.util.Lists;

import be.kuleuven.cs.distrinet.rejuse.property.Property;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;

/**
 * Support class for member-like elements that can be the direct children of a
 * type.
 * 
 * @author Marko van Dooren
 */
public abstract class TypeElementImpl extends ElementWithModifiersImpl implements TypeElement {

   @Override
   public List<? extends Member> declaredMembers() {
      try {
         return getIntroducedMembers();
      } catch (LookupException e) {
         throw new ChameleonProgrammerException(
               "This should not happen. Element of class "
                     + this.getClass().getName()
                     + " threw a lookup exception in getIntroducedMembers. This exception ended up in declaredMembers. But if that is the case, then declaredMembers must be overridden to provide a proper definition.",
               e);
      }
   }

   @Override
   public List<Modifier> modifiers(PropertyMutex mutex) throws ModelException {
      Property property = property(mutex);
      List<Modifier> result = Lists.create();
      for (Modifier mod : modifiers()) {
         if (mod.impliesTrue(property)) {
            result.add(mod);
         }
      }
      return result;
   }
}
