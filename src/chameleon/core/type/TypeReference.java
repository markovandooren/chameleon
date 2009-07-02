package chameleon.core.type;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceOrType;
import chameleon.core.namespace.NamespaceOrTypeReference;
import chameleon.core.reference.CrossReference;
import chameleon.core.relation.WeakPartialOrder;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class TypeReference extends NamespaceOrTypeReference<TypeReference,Type> implements CrossReference<TypeReference,Element> {

  public TypeReference(String qn) {
    super(getTarget(Util.getAllButLastPart(qn)), Util.getLastPart(qn));
  }
  
  public TypeReference(NamespaceOrTypeReference target, String name) {
    super(target, name);
  }
  
  public DeclarationSelector<Type> selector() {
	  return new DeclarationSelector<Type>() {

		@Override
		public Type filter(Declaration declaration) throws LookupException {
		  Type result;
		  if((declaration instanceof Type) && (((Type)declaration).signature().getName().equals(getName()))) {
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
						throws LookupException {
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

  public Type getType() throws LookupException {
    Type result = null;

    result = getCache();
    if(result != null) {
    	lookupLogger().debug("Hit cache for" + getFullyQualifiedName());
    	return result;
    }
    
    if(getTarget() != null) {
    	NamespaceOrType target = getTarget().getNamespaceOrType();
      if(target != null) {
        result = target.targetContext().lookUp(selector());//findType(getName());
      }
    }
    else {
      result = parent().lexicalContext(this).lookUp(selector()); //(getName());
    }
    
    if(result != null) {
    	setCache(result);
      return result;
    } else {
      throw new LookupException(selector());
    }
  }
  
  public Type getElement() throws LookupException {
  	return getType();
  }

  public TypeReference clone() {
    return new TypeReference((getTarget() == null ? null : getTarget().clone()),getName());
  }
  
}
