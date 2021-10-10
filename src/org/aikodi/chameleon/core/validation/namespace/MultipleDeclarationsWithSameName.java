package org.aikodi.chameleon.core.validation.namespace;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.core.validation.VerificationRule;

import java.util.*;

public class MultipleDeclarationsWithSameName<C extends DeclarationContainer> extends VerificationRule<C> {
   
   private List<Class<? extends Declaration>> _declarationType;
   
   public MultipleDeclarationsWithSameName(Class<C> containerType,
         Class<? extends Declaration>... declarationTypes) {
      super(containerType);
      _declarationType = Arrays.asList(declarationTypes);
   }

   public boolean handles(Declaration declaration) {
      for(Class<? extends Declaration> type: _declarationType) {
        if(type.isInstance(declaration)) {
           return true;
        }
      }
      return false;
   }
   
   public String names() {
      StringBuilder result = new StringBuilder();
      Iterator<Class<? extends Declaration>> iterator = _declarationType.iterator();
      while(iterator.hasNext()) {
         Class<? extends Declaration> type = iterator.next();
         result.append(type.getSimpleName());
         if(iterator.hasNext()) {
            result.append(", ");
         }
      }
      return result.toString();
   }
   
   @Override
   public Verification verify(C namespace) {
      Verification result = Valid.create();
      Set<String> names = new HashSet<>();
      Set<String> reported = new HashSet<>();
      try {
         for(Declaration declaration: namespace.declarations()) {
            if(handles(declaration)) {
               String name = declaration.name();
               if(names.contains(name)) {
                  if(!reported.contains(name)) {
                     reported.add(name);
                     result = result.and(
                           new BasicProblem(namespace, 
                                 "There cannot be multiple elements of type(s) "+
                           names() + " with name "+name
                                 ));
                  }
               } else {
                  names.add(name);
               }
            }
         }
      } catch (LookupException e) {
         //If the declarations cannot be computed, we report the problem
         result = result.and(new BasicProblem(namespace, "Cannot compute the declarations this container."));
      }
      return result;
   }
}