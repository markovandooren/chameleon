package org.aikodi.chameleon.oo.type.generics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeFixer;
import org.aikodi.chameleon.oo.type.TypeReference;

public class ExtendsWildcard extends TypeArgumentWithTypeReference {

  public ExtendsWildcard(TypeReference ref) {
    super(ref);
  }

  @Override
  public Type type() throws LookupException {
    ExtendsWildcardType extendsWildcardType = new ExtendsWildcardType(baseType());
    extendsWildcardType.setUniParent(this);
    return extendsWildcardType;
  }

  @Override
  protected ExtendsWildcard cloneSelf() {
    return new ExtendsWildcard(null);
  }

  @Override
  public Type lowerBound() throws LookupException {
    Type baseType = baseType();
    return baseType.language(ObjectOrientedLanguage.class).getNullType(view().namespace());
  }

  @Override
  public Type upperBound() throws LookupException {
    return baseType();
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

  /**
   * For wildcards {@code ? extends B} corresponding to parameter {@code Fi extends Ui}, the captured type parameter 
   * is Si with upper bound {@code glb(B, Ui[A1=S1,...,Ai=Si,...])} and lower bound the null type.
   * 
   * All constraints of the formal parameter are copied, and with a non-local reference, their
   * lookup path is redirected to the formal parameter such that all types in the bounds Ui are
   * resolved corrected. An additional constraint
   * with B as the upper bound is added, and its lookup path is redirected to this wildcard object
   * to correctly resolve any types in B.
   * 
   * <b>Any references to Ai must still replaced by references to Si. Capture conversion for 
   * derived types takes care of this.</b>  
   * 
   * Note that in practice, the name of the parameter is unchanged. It is not needed because we use
   * an object model instead of a string match as is typically done in type system. 
   */
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
    // Copy and redirect the bound of the formal parameter.
    for(TypeConstraint constraint: constraints) {
      newConstraints.add(cloneAndResetTypeReference(constraint,constraint));
    }
    // Copy and redirect the type argument
    newConstraints.add(cloneAndResetTypeReference(new ExtendsConstraint(clone(typeReference())),this));
    return newConstraints;
  }

  @Override
  public String toString(Set<Element> visited) {
    TypeReference tref = typeReference();
    StringBuffer result = new StringBuffer();
    result.append('?');
    if(tref != null) {
      result.append(" extends ");
      result.append(toStringTypeReference(visited));
    }
    return result.toString();
  }

  public boolean contains(TypeArgument other, TypeFixer trace) throws LookupException	{
    Type otherUpperBound = other.upperBound();
    Type upperBound = upperBound();
    boolean upperBoundNotHigherThan = otherUpperBound.subtypeOf(upperBound, trace);
    return upperBoundNotHigherThan;
  }

  @Override
  public boolean isWildCardBound() {
    return true;
  }

}
