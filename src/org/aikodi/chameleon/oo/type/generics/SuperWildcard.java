package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeFixer;
import org.aikodi.chameleon.oo.type.TypeReference;

import java.util.ArrayList;
import java.util.List;

public class SuperWildcard extends TypeArgumentWithTypeReference {

  public SuperWildcard(TypeReference ref) {
    super(ref);
  }

  @Override
  protected SuperWildcard cloneSelf() {
    return new SuperWildcard(null);
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
    SuperWildcardType superWildCardType = new SuperWildcardType(baseType());
    superWildCardType.setUniParent(this);
    return superWildCardType;
  }

  @Override
  public Type lowerBound() throws LookupException {
    return baseType();
  }

  @Override
  public Type upperBound() throws LookupException {
    return baseType().language(ObjectOrientedLanguage.class).getDefaultSuperClass(view().namespace());
  }

  public Type baseType() throws LookupException {
    TypeReference tref = typeReference();
    if(tref != null) {
      Type type = tref.getElement();
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
    CapturedTypeParameter newParameter = new CapturedTypeParameter(formal.name());
    capture(formal.constraints()).forEach(c -> newParameter.addConstraint(c));
    return newParameter;
  }

  /**
   * @param constraints
   * @return
   */
  public List<TypeConstraint> capture(List<TypeConstraint> constraints) {
    List<TypeConstraint> newConstraints = new ArrayList<>();
    for(TypeConstraint constraint: constraints) {
      TypeConstraint clone = cloneAndResetTypeReference(constraint,constraint);
      newConstraints.add(clone);
    }
    newConstraints.add(cloneAndResetTypeReference(new SuperConstraint(clone(typeReference())),this));
    return newConstraints;
  }

  @Override
  public String toString(java.util.Set<Element> visited) {
    TypeReference tref = typeReference();
    StringBuffer result = new StringBuffer();
    result.append("? super ");
    if(tref != null) {
      result.append(toStringTypeReference(visited));
    }
    return result.toString();
  }


  public boolean contains(TypeArgument other, TypeFixer trace) throws LookupException	{
    Type lowerBound = lowerBound();
    Type otherLowerBound = other.lowerBound();
    boolean lower = lowerBound.subtypeOf(otherLowerBound, trace);
    return lower;
  }

  @Override
  public boolean isWildCardBound() {
    return true;
  }

}
