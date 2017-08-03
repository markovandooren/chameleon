package org.aikodi.chameleon.oo.member;

import java.util.Iterator;
import java.util.List;

import org.aikodi.chameleon.core.declaration.SignatureWithName;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.method.DeclarationWithParameters;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.oo.type.generics.FormalTypeParameter;
import org.aikodi.chameleon.oo.type.generics.TypeConstraint;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;
import org.aikodi.chameleon.oo.variable.FormalParameter;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.util.association.Multi;

/**
 * A class of signatures that include parameter types.
 * 
 * @author Marko van Dooren
 */
public class SignatureWithParameters extends SignatureWithName {

   public SignatureWithParameters(String name) {
      super(name);
   }

	public boolean sameParameterBoundsAs(SignatureWithParameters other) throws LookupException {
		boolean after = sameParameterBoundsAsAfter(other);
 		return after;
	}
	
   // /*********************
   // * FORMAL PARAMETERS *
   // *********************/
   //
	
	/**
	 * @return the list of references to the types of the formal parameters.
	 */
   public List<TypeReference> typeReferences() {
      return _parameterTypes.getOtherEnds();
   }

   public void add(TypeReference arg) {
      add(_parameterTypes, arg);
   }

   public void addAll(List<TypeReference> trefs) {
      for (TypeReference tref : trefs) {
         add(tref);
      }
   }

   public void remove(TypeReference arg) {
      remove(_parameterTypes, arg);
   }

   public int nbTypeReferences() {
      return _parameterTypes.size();
   }

   private Multi<TypeReference> _parameterTypes = new Multi<TypeReference>(this, "parameter types");
   {
      _parameterTypes.enableCache();
   }

   @Override
   protected SignatureWithParameters cloneSelf() {
      return new SignatureWithParameters(name());
   }

   @Override
   public boolean uniSameAs(Element other) throws LookupException {
      boolean result = false;
      if (other instanceof SignatureWithParameters) {
         SignatureWithParameters sig = (SignatureWithParameters) other;
         result = name().equals(sig.name()) && sameParameterTypesAs(sig);
      }
      return result;
   }

   public List<Type> parameterTypes() throws LookupException {
      List<Type> result = Lists.create(_parameterTypes.size());
      for (TypeReference ref : typeReferences()) {
         result.add(ref.getElement());
      }
      return result;
   }

   @Override
   public Verification verifySelf() {
      Verification result = Valid.create();
      if (name() == null) {
         result = result.and(new BasicProblem(this, "The signature has no name."));
      }
      return result;
   }

   @Override
   public String toString() {
      StringBuilder result = new StringBuilder();
      result.append(name());
      result.append("(");
      List<TypeReference> types = typeReferences();
      int size = types.size();
      if (size > 0) {
         result.append(types.get(0).toString());
      }
      for (int i = 1; i < size; i++) {
         result.append(",");
         result.append(types.get(i).toString());
      }
      result.append(")");
      return result.toString();
   }

   public int nbFormalParameters() {
      return _parameterTypes.size();
   }

   @Override
   public boolean hasMorePropertiesThanName() {
      return true;
   }


	@SuppressWarnings("unused")
	private boolean sameParameterBoundsAsAfter(SignatureWithParameters other) throws LookupException {
  	// substitute paramaters.
		DeclarationWithParameters otherMethod = other.lexical().nearestAncestor(DeclarationWithParameters.class);
  	DeclarationWithParametersHeader otherHeader = otherMethod.header();
  	
  	int nbOtherFormalParameters = otherHeader.nbFormalParameters();
  	int nbMyFormalParameters = nbFormalParameters();
  	boolean result = nbOtherFormalParameters == nbMyFormalParameters;
  	
  	if(result) {
  		DeclarationWithParameters nearestAncestor = lexical().nearestAncestor(DeclarationWithParameters.class);
  		int nbMyTypeParameters = nearestAncestor.nbTypeParameters();
  		int nbOtherTypeParameters = other.lexical().nearestAncestor(DeclarationWithParameters.class).nbTypeParameters();
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
					boolean clonedSubtypeOfMine = clonedParameterType.subtypeOf(myParameterType);
					result = clonedSubtypeOfMine && myParameterType.subtypeOf(clonedParameterType);
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
	
  public boolean sameParameterTypesAs(SignatureWithParameters other) throws LookupException {
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
