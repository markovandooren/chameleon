package chameleon.oo.member;

import java.util.Iterator;
import java.util.List;

import chameleon.core.declaration.Signature;
import chameleon.core.lookup.LookupException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.method.Method;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.oo.type.generics.ExtendsConstraint;
import chameleon.oo.type.generics.FormalTypeParameter;
import chameleon.oo.type.generics.TypeParameter;
import chameleon.oo.variable.FormalParameter;

public abstract class DeclarationWithParametersSignature extends Signature {

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

	public abstract DeclarationWithParametersSignature clone();
	
	public abstract String name();
	
	public abstract int nbFormalParameters();
	
	public abstract List<Type> parameterTypes() throws LookupException;
	
	public abstract List<TypeReference> typeReferences();
	
	public boolean sameParameterBoundsAs(DeclarationWithParametersSignature other) throws LookupException {
		boolean after = sameParameterBoundsAsAfter(other);
 		return after;
	}

	private boolean sameParameterBoundsAsAfter(DeclarationWithParametersSignature other) throws LookupException {
  	// substitute paramaters.
  	Method method = (Method)other.nearestAncestor(Method.class);
  	DeclarationWithParametersHeader otherHeader = method.header();
  	int nbOtherFormalParameters = otherHeader.nbFormalParameters();
  	int nbMyFormalParameters = nbFormalParameters();
  	boolean result = nbOtherFormalParameters == nbMyFormalParameters;
  	if(result) {
  		DeclarationWithParametersHeader clonedHeader = otherHeader.clone();
  		clonedHeader.setUniParent(method);
  		List<TypeParameter> cloneTypeParameters = clonedHeader.typeParameters();
  		List<TypeParameter> myTypeParameters = nearestAncestor(Method.class).typeParameters();
  		int size = myTypeParameters.size();
  		result = (size == cloneTypeParameters.size());
  		if(result) {
  			// FIXME: part of this should be delegated to 'other' and a class Erased...Signature should be made
  			//        to avoid cloning when it is not necessary (and to clean up this bad code of course).
  			List<FormalParameter> clonedFormalParameters = (List<FormalParameter>)clonedHeader.formalParameters();
  			List<TypeReference> typeReferencesForComparison = other.typeReferences();
  			for(int i=0; i < nbMyFormalParameters; i++) {
  				clonedFormalParameters.get(i).setTypeReference(typeReferencesForComparison.get(i).clone());
  			}
  			for(int i=0; i < size; i++) {
  				TypeParameter myTypeParameter = myTypeParameters.get(i);
  				TypeParameter clonedTypeParameter = cloneTypeParameters.get(i);
  				TypeReference replacement = language(ObjectOrientedLanguage.class).createTypeReference(myTypeParameter.signature().name());
  				replacement.setUniParent(myTypeParameter.parent());
  				
  				// substitute in formal parameter types
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
	
//	public boolean sameParameterBoundsAsBefore(DeclarationWithParametersSignature other) throws LookupException {
//  	// substitute paramaters.
//  	Method method = (Method)other.nearestAncestor(Method.class);
//  	DeclarationWithParametersHeader otherHeader = method.header();
//  	int nbOtherFormalParameters = otherHeader.nbFormalParameters();
//  	int nbMyFormalParameters = nbFormalParameters();
//  	boolean result = nbOtherFormalParameters == nbMyFormalParameters;
//  	if(result) {
//  		DeclarationWithParametersHeader clonedHeader = otherHeader.clone();
//  		clonedHeader.setUniParent(method);
//  		List<TypeParameter> cloneTypeParameters = clonedHeader.typeParameters();
//  		List<TypeParameter> myTypeParameters = nearestAncestor(Method.class).typeParameters();
//  		int size = myTypeParameters.size();
//  		result = (size == cloneTypeParameters.size());
//  		if(result) {
//  			List<FormalParameter> clonedFormalParameters = (List<FormalParameter>)clonedHeader.formalParameters();
//  			for(int i=0; i < size; i++) {
//  				TypeParameter myTypeParameter = myTypeParameters.get(i);
//  				TypeParameter clonedTypeParameter = cloneTypeParameters.get(i);
//  				TypeReference replacement = language(ObjectOrientedLanguage.class).createTypeReference(myTypeParameter.signature().name());
//  				replacement.setUniParent(myTypeParameter.parent());
//  				// substitute in formal parameters
//  				for(FormalParameter formal: clonedFormalParameters) {
//  					language(ObjectOrientedLanguage.class).replace(replacement, clonedTypeParameter, formal.getTypeReference());
//  				}
//
//  				// substitute in type bounds of the type parameters of the cloned header.
//  				for(TypeParameter typeParameter: (List<TypeParameter>)clonedHeader.typeParameters()) {
//  					if(typeParameter instanceof FormalTypeParameter) {
//  						FormalTypeParameter formal = (FormalTypeParameter) typeParameter;
//  						language(ObjectOrientedLanguage.class).replace(replacement, clonedTypeParameter, ((ExtendsConstraint)formal.constraints().get(0)).typeReference());
//  					}
//  				}
//  			}
//  			List<Type> myFormalParameterTypes = parameterTypes();
//  			for(int i=0; result && i < nbMyFormalParameters; i++) {
//  				result = clonedFormalParameters.get(i).getType().sameAs(myFormalParameterTypes.get(i));
//  			}
//  			for(int i=0; result && i < size; i++) {
//  				result = cloneTypeParameters.get(i).upperBound().sameAs(myTypeParameters.get(i).upperBound());
//  			}
//  		}
//  	}
//  	return result;
//	}

  public boolean sameParameterTypesAs(DeclarationWithParametersSignature other) throws LookupException {
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
