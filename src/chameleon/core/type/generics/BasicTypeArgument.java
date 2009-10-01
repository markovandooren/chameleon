package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

/**
 * A class of type arguments that consist of a type name.
 * 
 * @author Marko van Dooren
 */
public class BasicTypeArgument extends ActualTypeArgumentWithTypeReference<BasicTypeArgument> {


	public BasicTypeArgument(TypeReference ref) {
		super(ref);
	}

	@Override
	public BasicTypeArgument clone() {
		return new BasicTypeArgument(typeReference().clone());
	}

 /*@
   @ public behavior
   @
   @ post \result == baseType();
   @*/
	@Override
	public Type type() throws LookupException {
		return baseType();
	}

 /*@
   @ public behavior
   @
   @ post \result == baseType();
   @*/
	@Override
	public Type lowerBound() throws LookupException {
		return baseType();
	}

 /*@
   @ public behavior
   @
   @ post \result == baseType();
   @*/
	@Override
	public Type upperBound() throws LookupException {
		return baseType();
	}

	public Type baseType() throws LookupException {
		TypeReference tref = typeReference();
		if(tref != null) {
			Type type = tref.getType();
			if(type != null) {
			  return type;
			} else {
				throw new LookupException("Lookup of type of generic argument return null."); 
			}
		} else {
			throw new LookupException("Generic argument has no type reference.");
		}
	}

	@Override
	public TypeParameter capture(FormalTypeParameter formal) {
		return parent();
	}
}
