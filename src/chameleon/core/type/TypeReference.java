package chameleon.core.type;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationSelector;
import chameleon.core.namespace.NamespaceOrType;
import chameleon.core.namespace.NamespaceOrTypeReference;
import chameleon.core.reference.CrossReference;
import chameleon.core.relation.WeakPartialOrder;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class TypeReference extends NamespaceOrTypeReference<TypeReference,Type> implements CrossReference {

  public TypeReference(String qn) {

    super(getTarget(Util.getAllButLastPart(qn)), Util.getLastPart(qn));
//	String s1 = Util.getAllButLastPart(qn);
//    String s2 = Util.getLastPart(qn);
//    NamespaceOrTypeReference notr = getTarget(Util.getAllButLastPart(qn)); 
  }
  
  public TypeReference(NamespaceOrTypeReference target, String name) {
    super(target, name);
  }
  
  protected static NamespaceOrTypeReference getTarget(String qn) {
    if(qn == null) {
      return null;
    }
    NamespaceOrTypeReference target = new NamespaceOrTypeReference(null, Util.getFirstPart(qn));
    qn = Util.getSecondPart(qn);
    while(qn != null) {
    	NamespaceOrTypeReference newTarget = new NamespaceOrTypeReference(null, Util.getFirstPart(qn));
      newTarget.setTarget(target);
      target = newTarget;
      qn = Util.getSecondPart(qn);
    }
    return target;
  }
  
  public DeclarationSelector<Type> selector() {
	  return new DeclarationSelector<Type>() {

		@Override
		public Type filter(Declaration declaration) throws MetamodelException {
		  Type result;
		  if((declaration instanceof Type) && (((Type<Type>)declaration).signature().getName().equals(getName()))) {
			result = (Type) declaration;
		  } else {
			result = null;
		  }
		  return result;
		}

		@Override
		public WeakPartialOrder<Type> order() {
			return new WeakPartialOrder<Type>() {

				@Override
				public boolean contains(Type first, Type second)
						throws MetamodelException {
					return first.equals(second);
				}
				
			};
		}

		@Override
		public Class<Type> selectedClass() {
			return Type.class;
		}
		  
	  };
  }

  public Type getType() throws MetamodelException {
    Type result = null;

    result = getCache();
    if(result != null) {
    	return result;
    }
    
    if(getTarget() != null) {
    	NamespaceOrType target = getTarget().getNamespaceOrType();
      if(target != null) {
        result = target.targetContext().lookUp(selector());//findType(getName());
      }
    }
    else {
      result = getParent().lexicalContext(this).lookUp(selector()); //(getName());
    }
    
    if(result != null) {
    	setCache(result);
      return result;
    } else {
      throw new MetamodelException();
    }
  }
  
  public Type getElement() throws MetamodelException {
  	return getType();
  }

  public TypeReference clone() {
    return new TypeReference(getTarget().clone(),getName());
  }
  
}
