package chameleon.core.method;

import java.util.Iterator;
import java.util.List;

import chameleon.core.declaration.Signature;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElement;
import chameleon.core.variable.FormalParameter;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.oo.type.generics.ExtendsConstraint;
import chameleon.oo.type.generics.FormalTypeParameter;
import chameleon.oo.type.generics.TypeParameter;

public abstract class MethodSignature<E extends MethodSignature,P extends NamespaceElement> extends Signature<E, P> {

//	public String name() {
//		return _name;
//	}
//	
//	private String _name;
//	
//  /*********************
//   * FORMAL PARAMETERS *
//   *********************/
//
//  public List<TypeReference> typeReferences() {
//    return _parameters.getOtherEnds();
//  }
//
//
//  public void add(TypeReference arg) {
//    _parameters.add(arg.parentLink());
//  }
//
//  public int getNbTypeReferences() {
//    return _parameters.size();
//  }
//
//  private OrderedReferenceSet<E,TypeReference> _parameters = new OrderedReferenceSet<E,TypeReference>((E) this);

	public abstract E clone();
	
	public abstract String name();
	
	public abstract int nbFormalParameters();
	
	public abstract List<Type> parameterTypes() throws LookupException;
	
	public boolean sameParameterBoundsAs(MethodSignature other) throws LookupException {
  	// substitute paramaters.
  	Method method = (Method)other.nearestAncestor(Method.class);
  	MethodHeader otherHeader = method.header();
  	int nbOtherFormalParameters = otherHeader.nbFormalParameters();
  	int nbMyFormalParameters = nbFormalParameters();
  	boolean result = nbOtherFormalParameters == nbMyFormalParameters;
  	if(result) {
  		MethodHeader clonedHeader = otherHeader.clone();
  		clonedHeader.setUniParent(method);
  		List<TypeParameter> cloneTypeParameters = clonedHeader.typeParameters();
  		List<TypeParameter> myTypeParameters = nearestAncestor(Method.class).typeParameters();
  		int size = myTypeParameters.size();
  		result = (size == cloneTypeParameters.size());
  		if(result) {
  			List<FormalParameter> clonedFormalParameters = (List<FormalParameter>)clonedHeader.formalParameters();
  			for(int i=0; i < size; i++) {
  				TypeParameter myTypeParameter = myTypeParameters.get(i);
  				TypeParameter clonedTypeParameter = cloneTypeParameters.get(i);
  				TypeReference replacement = language(ObjectOrientedLanguage.class).createTypeReference(myTypeParameter.signature().name());
  				replacement.setUniParent(myTypeParameter.parent());
  				// substitute in formal parameters
  				for(FormalParameter formal: clonedFormalParameters) {
  					language(ObjectOrientedLanguage.class).replace(replacement, clonedTypeParameter, formal.getTypeReference());
  				}

  				// substitute in type bounds of the type parameters of the cloned header.
  				for(TypeParameter typeParameter: (List<TypeParameter>)clonedHeader.typeParameters()) {
  					if(typeParameter instanceof FormalTypeParameter) {
  						FormalTypeParameter formal = (FormalTypeParameter) typeParameter;
  						language(ObjectOrientedLanguage.class).replace(replacement, clonedTypeParameter, ((ExtendsConstraint)formal.constraints().get(0)).typeReference());
  					}
  				}
  			}
  			List<Type> myFormalParameterTypes = parameterTypes();
  			for(int i=0; result && i < nbMyFormalParameters; i++) {
  				result = clonedFormalParameters.get(i).getType().sameAs(myFormalParameterTypes.get(i));
  			}
  			for(int i=0; result && i < size; i++) {
  				result = cloneTypeParameters.get(i).upperBound().sameAs(myTypeParameters.get(i).upperBound());
  			}
  		}
  	}
  	return result;
	}
	
  public boolean sameParameterTypesAs(MethodSignature other) throws LookupException {
  	boolean result = false;
  	if (other != null) {
			List<Type> mine = parameterTypes();
			List<Type> others = other.parameterTypes();
			result = mine.size() == others.size();
			Iterator<Type> iter1 = mine.iterator();
			Iterator<Type> iter2 = others.iterator();
			while (result && iter1.hasNext()) {
        result = result && iter1.next().sameAs(iter2.next());
			}
		}
  	return result;
  }
  
}
