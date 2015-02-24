package be.kuleuven.cs.distrinet.chameleon.core.reference;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.TargetDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.NameSelector;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

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
    * The class object for the type of declaration that is referenced.
    */
   private Class<D> _specificClass;

   /**
    * Initialize a new simple reference given a fully qualified name. The name
    * is split at every dot, and multiple objects are created to form a chain of
    * references.
    * 
    * The prefixes are all references to {@link TargetDeclaration}s. If the
    * given type must be used for all prefixes, use constructor
    * {@link #NameReference(String, Class, boolean)} with true as the last
    * argument.
    * 
    * @param fqn
    *           The fully qualified name of the referenced declaration.
    * @param type
    *           The type of declaration that is referenced.
    */
   public NameReference(String fqn, Class<D> type) {
      this(fqn, type, false);
   }

   /**
    * Initialize a new simple reference given a fully qualified name. The name
    * is split at every dot, and multiple objects are created to form a chain of
    * references.
    * 
    * If recusriveLimit is true, the prefixes are all references to elements of
    * the given type. If recursiveLimit is false, the prefixes are all
    * references to {@link TargetDeclaration}s.
    * 
    * @param fqn
    *           The fully qualified name of the referenced declaration.
    * @param type
    *           The type of declaration that is referenced.
    * @param recursiveLimit
    *           Indicate whether the type restriction must be applied to the
    *           prefixes.
    */
   public NameReference(String fqn, Class<D> type, boolean recursiveLimit) {
      this(null, Util.getLastPart(fqn), type);
      setTarget(createTarget(fqn, type, recursiveLimit));
   }

   /**
    * Initialize a new simple reference given a fully qualified name. The name
    * is split at every dot, and multiple objects are created to form a chain of
    * references.
    *
    * @param target
    *           A cross-reference to the target declaration in which this
    *           name reference must be resolved.
    * @param name
    *           The name of the element referenced by this name reference.
    * @param type
    *           The type of declaration that is referenced.
    */
   public NameReference(CrossReferenceTarget target, String name, Class<D> type) {
      super(name);
      setTarget(target);
      _specificClass = type;
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
         _selector = new NameSelector<D>(_specificClass) {
            @Override
            public String name() {
               return NameReference.this.name();
            }
         };
      }
      return _selector;
   }

   /**
    * Return the {@link Class} object of the kind of elements that this
    * reference can point at.
    * 
    * @return
    */
   public Class<D> referencedType() {
      return _specificClass;
   }

   protected NameReference createTarget(String fqn, Class specificClass, boolean recursiveLimit) {
      String allButLastPart = Util.getAllButLastPart(fqn);
      if (allButLastPart == null) {
         return null;
      } else {
         return createSimpleReference(allButLastPart, recursiveLimit ? specificClass : TargetDeclaration.class,
               recursiveLimit);
      }
   }

   /**
    * Subclasses must override this method and return an object of the type of
    * the subclass.
    * 
    * @param fqn
    *           The fully qualified name of the referenced declaration.
    * @param type
    *           The type of declaration that is referenced.
    * @param recursiveLimit
    *           Indicate whether the type restriction must be applied to the
    *           prefixes.
    * @return
    */
   protected <D extends Declaration> NameReference<D> createSimpleReference(String fqn, Class<D> kind,
         boolean recursiveLimit) {
      return new NameReference(fqn, recursiveLimit ? kind : TargetDeclaration.class, recursiveLimit);
   }

}
