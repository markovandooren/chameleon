package chameleon.core.type;

import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.ElementReference;
import chameleon.core.reference.SpecificReference;

/**
 * @author Marko van Dooren
 */
public class TypeReference extends SpecificReference<TypeReference,Element,Type> {

  public TypeReference(String qn) {
    super(qn, Type.class);
  }
  
  public TypeReference(ElementReference<?, ?, ? extends TargetDeclaration> target, String name) {
    super(target, name, Type.class);
  }
  
//  public DeclarationSelector<Type> selector() {
//	  return new DeclarationSelector<Type>() {
//
//		@Override
//		public Type filter(Declaration declaration) throws LookupException {
//		  Type result;
//		  if((declaration instanceof Type) && (((Type)declaration).signature().getName().equals(getName()))) {
//			result = (Type) declaration;
//		  } else {
//			result = null;
//		  }
//		  return result;
//		}
//
//		@Override
//		public WeakPartialOrder<Type> order() {
//			return new WeakPartialOrder<Type>() {
//
//				@Override
//				public boolean contains(Type first, Type second)
//						throws LookupException {
//					return first.equals(second);
//				}
//				
//			};
//		}
//
//		@Override
//		public Class<Type> selectedClass() {
//			return Type.class;
//		}
//		  
//	  };
//  }
//
//  public Type getType() throws LookupException {
//    Type result = null;
//
//    result = getCache();
//    if(result != null) {
//    	return result;
//    }
//    
//    if(getTarget() != null) {
//    	NamespaceOrType target = getTarget().getNamespaceOrType();
//      if(target != null) {
//        result = target.targetContext().lookUp(selector());//findType(getName());
//      }
//    }
//    else {
//      result = parent().lexicalLookupStrategy(this).lookUp(selector()); //(getName());
//    }
//    
//    if(result != null) {
//    	setCache(result);
//      return result;
//    } else {
//      throw new LookupException(selector());
//    }
//  }
//  
//  public Type getElement() throws LookupException {
//  	return getType();
//  }
  
  public Type getType() throws LookupException {
  	return getElement();
  }

  public TypeReference clone() {
    return new TypeReference((getTarget() == null ? null : getTarget().clone()),getName());
  }
  
}
