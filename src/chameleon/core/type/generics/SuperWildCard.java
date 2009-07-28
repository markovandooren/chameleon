package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.element.Element;
import chameleon.core.language.ObjectOrientedLanguage;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;

public class SuperWildCard extends ActualTypeArgument<SuperWildCard> {

	private Reference<ActualTypeArgument,TypeReference> _type = new Reference<ActualTypeArgument,TypeReference>(this);

	public SuperWildCard(TypeReference ref) {
		setTypeReference(ref);
	}

	@Override
	public SuperWildCard clone() {
		return new SuperWildCard(typeReference().clone());
	}

//	@Override
//	public boolean contains(ActualTypeArgument other) throws LookupException {
//		return (
//		     (other instanceof BasicTypeArgument) && 
//		     (baseType().subTypeOf(((BasicTypeArgument)other).type()))
//		   ) ||
//		   (
//		  	 (other instanceof SuperWildCard) &&
//		  	 (baseType().subTypeOf(((ExtendsWildCard)other).baseType()))
//		   );
//	}

	@Override
	public Type type() throws LookupException {
		return new SuperWildCardType(baseType());
	}

	@Override
	public Type lowerBound() throws LookupException {
		return baseType();
	}

	@Override
	public Type upperBound() throws LookupException {
		return baseType().language(ObjectOrientedLanguage.class).getDefaultSuperClass();
	}

	public List<? extends Element> children() {
		List<Element> result = new ArrayList<Element>();
		result.add(typeReference());
		return result;
	}

	public TypeReference typeReference() {
		return _type.getOtherEnd();
	}

	public void setTypeReference(TypeReference ref) {
		if(ref == null) {
			_type.connectTo(null);
		} else {
			_type.connectTo(ref.parentLink());
		}
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
		CapturedTypeParameter newParameter = new CapturedTypeParameter(formal.signature().clone());
		for(TypeConstraint constraint: formal.constraints()) {
			newParameter.addConstraint(constraint.clone());
		}
		newParameter.addConstraint(new SuperConstraint(typeReference().clone()));
    return newParameter;
	}

}
