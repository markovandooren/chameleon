package org.aikodi.chameleon.oo.member;

import java.util.Iterator;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.method.DeclarationWithParameters;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.oo.type.generics.FormalTypeParameter;
import org.aikodi.chameleon.oo.type.generics.TypeConstraint;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;
import org.aikodi.chameleon.oo.variable.FormalParameter;

public abstract class DeclarationWithParametersSignature extends Signature {

	@Override
   public abstract String name();
	
	public abstract int nbFormalParameters();
	
	public abstract List<Type> parameterTypes() throws LookupException;
	
	public abstract List<TypeReference> typeReferences();
	
	public boolean sameParameterBoundsAs(DeclarationWithParametersSignature other) throws LookupException {
		boolean after = sameParameterBoundsAsAfter(other);
 		return after;
	}

	@SuppressWarnings("unused")
	private boolean sameParameterBoundsAsAfter(DeclarationWithParametersSignature other) throws LookupException {
  	// substitute paramaters.
		DeclarationWithParameters otherMethod = other.nearestAncestor(DeclarationWithParameters.class);
  	DeclarationWithParametersHeader otherHeader = otherMethod.header();
  	
  	int nbOtherFormalParameters = otherHeader.nbFormalParameters();
  	int nbMyFormalParameters = nbFormalParameters();
  	boolean result = nbOtherFormalParameters == nbMyFormalParameters;
  	
  	if(result) {
  		DeclarationWithParameters nearestAncestor = nearestAncestor(DeclarationWithParameters.class);
  		int nbMyTypeParameters = nearestAncestor.nbTypeParameters();
  		int nbOtherTypeParameters = other.nearestAncestor(DeclarationWithParameters.class).nbTypeParameters();
  		result = (nbMyTypeParameters == nbOtherTypeParameters);
  		if(result) {
				List<TypeParameter> myTypeParameters = nearestAncestor.typeParameters();
  			DeclarationWithParametersHeader clonedOtherHeader = clone(otherHeader);
  			clonedOtherHeader.setUniParent(otherMethod);
  			List<TypeParameter> cloneTypeParameters = clonedOtherHeader.typeParameters();
  			// FIXME: part of this should be delegated to 'other' and a class Erased...Signature should be made
  			//        to avoid cloning when it is not necessary (and to clean up this bad code of course).
  			List<FormalParameter> clonedFormalParameters = clonedOtherHeader.formalParameters();
  			for(int i=0; i < nbMyTypeParameters; i++) {
  				TypeParameter myTypeParameter = myTypeParameters.get(i);
  				TypeParameter clonedTypeParameter = cloneTypeParameters.get(i);
  				TypeReference replacement = language(ObjectOrientedLanguage.class).createTypeReference(myTypeParameter.name());
  				replacement.setUniParent(myTypeParameter.parent());
  				
  				// substitute in formal parameter types
  				for(FormalParameter formal: clonedFormalParameters) {
  					language(ObjectOrientedLanguage.class).replace(replacement, clonedTypeParameter, formal.getTypeReference());
  				}

  				// substitute in type bounds of the type parameters of the cloned header.
  				List<TypeParameter> typeParameters = clonedOtherHeader.typeParameters();
					for(TypeParameter typeParameter: typeParameters) {
  					if(typeParameter instanceof FormalTypeParameter) {
  						FormalTypeParameter formal = (FormalTypeParameter) typeParameter;
  						List<TypeConstraint> constraints = formal.constraints(); 
  						for(TypeConstraint constraint: constraints) {
  							language(ObjectOrientedLanguage.class).replace(replacement, clonedTypeParameter, constraint.typeReference());
  						}
  					}
  				}
  			}
  			List<Type> myFormalParameterTypes = parameterTypes();
  			for(int i=0; result && i < nbMyFormalParameters; i++) {
  				Type clonedParameterType = clonedFormalParameters.get(i).getType();
					Type myParameterType = myFormalParameterTypes.get(i);
//					boolean sameUpperBound = clonedParameterType.upperBound().sameAs(myParameterType.upperBound());
//					boolean sameLowerBound = clonedParameterType.lowerBound().sameAs(myParameterType.lowerBound());
					boolean clonedSubtypeOfMine = clonedParameterType.subTypeOf(myParameterType);
					result = clonedSubtypeOfMine && myParameterType.subTypeOf(clonedParameterType);
//					result = clonedParameterType.sameAs(myParameterType);
  			}
  			for(int i=0; result && i < nbMyTypeParameters; i++) {
  				// According to the language specification, the equality should be on the bounds, not just the upper bounds.
  				// However, in Java, type parameters of a method can have only extends constraints.
  				// FIXME Nevertheless, it would be better if TypeConstraint was a composite that could do
  				//       the check for us.
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
