package org.aikodi.chameleon.oo.expression;

import java.util.HashSet;
import java.util.Set;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.TargetDeclaration;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.NameSelector;
import org.aikodi.chameleon.core.reference.CommonCrossReferenceWithTarget;
import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.core.reference.CrossReferenceWithName;
import org.aikodi.chameleon.core.reference.CrossReferenceWithTarget;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.util.Util;

/**
 * A named reference to a declaration.
 * 
 * @author Marko van Dooren
 */
public class NamedTarget extends CommonCrossReferenceWithTarget<Declaration> implements CrossReferenceTarget,
      CrossReferenceWithTarget<Declaration>, CrossReferenceWithName<Declaration> {

   /**
    * Initialize a new named target with the given fully qualified name. The
    * constructor will split up the name at the dots if there are any.
    * 
    * @param fullyQualifiedName The fully qualified name of the referenced declaration.
    */
  /*@
    @ public behavior
    @
    @ pre fullyQualifiedName != null;
    @*/
   public NamedTarget(String fullyQualifiedName, ExpressionFactory factory) {
      super(Util.getAllButLastPart(fullyQualifiedName) == null ? null :  factory.createNamedTarget(Util.getAllButLastPart(fullyQualifiedName)));
      setName(Util.getLastPart(fullyQualifiedName));
   }

   protected NamedTarget() {
      super(null);
   }


   /**
    * Initialize a new named target with the given identifier as name, and the
    * given target as its target. The name should be an identifier and thus not
    * contain dots.
    * 
    * @param identifier
    * @param target
    */
   public NamedTarget(String identifier, CrossReferenceTarget target) {
      super(target);
      setName(identifier);
   }

   /***********
    * CONTEXT *
    ***********/

   @Override
   public DeclarationSelector<Declaration> selector() {
      if (_selector == null) {
         _selector = new NameSelector<Declaration>(Declaration.class) {
            @Override
            public String name() {
               return NamedTarget.this.name();
            }
         };
      }
      return _selector;
   }

   private DeclarationSelector<Declaration> _selector;

   /********
    * NAME *
    ********/

   @Override
   public String name() {
      return _name;
   }

   @Override
   public void setName(String name) {
      _name = name;
   }

   private String _name;

   @Override
   protected NamedTarget cloneSelf() {
      return new NamedTarget(name(), (CrossReferenceTarget) null);
   }

   @SuppressWarnings("unchecked")
   public Set getDirectExceptions() throws LookupException {
      Set result = new HashSet();
      if (getTarget() != null) {
         Util.addNonNull(language(ObjectOrientedLanguage.class).getNullInvocationException(view().namespace()), result);
      }
      return result;
   }

   @Override
   public String toString() {
      return name();
   }
}
