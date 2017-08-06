package org.aikodi.chameleon.core.reference;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.NameSelector;
import org.aikodi.chameleon.util.Util;

/**
 * A default implementation for cross-reference that consist of only a name.
 * 
 * @author Marko van Dooren
 *
 * @param <D>
 *           The type of the declaration that is referenced.
 */
public class NameReference<D extends Declaration> extends ElementReference<D> {

   /**
    * Initialize a new simple reference given a qualified name. The name
    * is split at every dot, and multiple objects are created to form a chain of
    * references.
    * 
    * The prefixes are all references to {@link Declaration}s. If the
    * given type must be used for all prefixes, use constructor
    * {@link #NameReference(String, Class, boolean)} with true as the last
    * argument.
    * 
    * @param qualifiedName
    *           The qualified name of the referenced declaration. The exact
    *           meaning of the name is determined by the lookup rules of the
    *           language. If the name contains dots, the last part will be the
    *           name of this name reference, and the prefix will be used
    *           to create another NameReference that will be the {@link #getTarget()}
    *           of this NameReference.
    * @param type
    *           The type of declaration that is referenced.
    */
   public NameReference(String qualifiedName, Class<D> type) {
      this(qualifiedName, type, false);
   }

   /**
    * Initialize a new simple reference given a qualified name. The name
    * is split at every dot, and multiple objects are created to form a chain of
    * references.
    * 
    * If recursiveLimit is true, the prefixes are all references to elements of
    * the given type. If recursiveLimit is false, the prefixes are all
    * references to {@link Declaration}s.
    * 
    * @param qualifiedName
    *           The qualified name of the referenced declaration.
    * @param type
    *           The type of declaration that is referenced.
    * @param recursiveLimit
    *           Indicate whether the type restriction must be applied to the
    *           prefixes.
    */
   public NameReference(String qualifiedName, Class<D> type, boolean recursiveLimit) {
      this(null, Util.getLastPart(qualifiedName), type);
      setTarget(createTarget(qualifiedName, type, recursiveLimit));
   }

   /**
    * Initialize a new simple reference given a qualified name. The name
    * is split at every dot, and multiple objects are created to form a chain of
    * references.
    *
    * @param target
    *           A cross-reference to the target declaration in which this
    *           name reference must be resolved.
    * @param qualifiedName
    *           The qualified name of the element referenced by this name reference.
    * @param type
    *           The type of declaration that is referenced.
    */
   public NameReference(CrossReferenceTarget target, String qualifiedName, Class<D> type) {
      super(qualifiedName, type);
      setTarget(target);
   }

   /**
    * YOU MUST OVERRIDE THIS METHOD IF YOU SUBCLASS THIS CLASS!
    */
   @Override
   protected NameReference<D> cloneSelf() {
      return new NameReference<D>(null, name(), referencedType());
   }

   private DeclarationSelector<D> _selector;

   @Override
   public DeclarationSelector<D> selector() {
      if (_selector == null) {
         _selector = new NameSelector<D>(referencedType()) {
            @Override
            public String name() {
               return NameReference.this.name();
            }
         };
      }
      return _selector;
   }

   protected NameReference createTarget(String fqn, Class specificClass, boolean recursiveLimit) {
      String allButLastPart = Util.getAllButLastPart(fqn);
      if (allButLastPart == null) {
         return null;
      } else {
         return createSimpleReference(allButLastPart, recursiveLimit ? specificClass : Declaration.class,
               recursiveLimit);
      }
   }

   /**
    * Subclasses must override this method and return an object of the type of
    * the subclass.
    * 
    * @param qualifiedName
    *           The qualified name of the referenced declaration.
    * @param type
    *           The type of declaration that is referenced.
    * @param recursiveLimit
    *           Indicate whether the type restriction must be applied to the
    *           prefixes.
    * @return
    */
   protected <D extends Declaration> NameReference<D> createSimpleReference(String qualifiedName, Class<D> kind,
         boolean recursiveLimit) {
      return new NameReference(qualifiedName, recursiveLimit ? kind : Declaration.class, recursiveLimit);
   }

}
