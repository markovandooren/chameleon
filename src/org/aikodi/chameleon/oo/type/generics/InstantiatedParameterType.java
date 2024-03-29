/**
 * 
 */
package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeFixer;
import org.aikodi.chameleon.oo.type.TypeIndirection;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.util.Util;

import java.util.List;
import java.util.Set;

public class InstantiatedParameterType extends TypeIndirection {

  public InstantiatedParameterType(String name, Type aliasedType, TypeParameter parameter) {
    super(name,aliasedType);
    setParameter(parameter);
  }

  @Override
  public String toString() {
    return parameter().toString();
  }

  @Override
  public List<Type> getProperDirectSuperTypes() throws LookupException {
    //			return aliasedType().getDirectSuperTypes();
    return Util.createNonNullList(indirectionTarget());
  }
  
  /**
   * Return the type aliased by this type.
   */
  public Type aliasedType() {
  	return indirectionTarget();
  }

  @Override
  public void accumulateAllSuperTypes(Set<Type> acc) throws LookupException {
    Type aliased =indirectionTarget();
    boolean add=true;
    for(Type acced: acc) {
      if(acced == aliased) {
        add=false;
        break;
      }
    }
    if(add) {
      acc.add(aliased);
      aliased.accumulateAllSuperTypes(acc);
    }
  }


  @Override
  public void newAccumulateAllSuperTypes(Set<Type> acc) throws LookupException {
    Type aliased =indirectionTarget();
    boolean add=true;
    for(Type acced: acc) {
    	//FIXME Why isn't this an equals call?
      if(acced == aliased) {
        add=false;
        break;
      }
    }
    if(add) {
      aliased.newAccumulateSelfAndAllSuperTypes(acc);
    }
  }


  @Override
  public InstantiatedParameterType cloneSelf() {
    return new InstantiatedParameterType(name(), indirectionTarget(),parameter());
  }

  @Override
  public Type actualDeclaration() {
    return this;
  }

  @Override
  public boolean uniSameAs(Element element) throws LookupException {
    boolean result = false;
    if(element instanceof InstantiatedParameterType) {
      result = parameter().sameAs(((InstantiatedParameterType)element).parameter());
    } 
    if(! result) {
      result = element.sameAs(indirectionTarget());
    }
    return result;
  }

  @Override
  public boolean uniSameAs(Type other, TypeFixer trace) throws LookupException {

    boolean result = false;
    if(other instanceof InstantiatedParameterType) {
      TypeParameter mine = parameter();
      TypeParameter others = ((InstantiatedParameterType)other).parameter();
      result = mine.sameAs(others);
      if(! result) {
        TypeParameter firstParam = parameter();
        if(trace.contains(other, firstParam)) {
          return true;
        }
        trace.add(other, firstParam);
      }
    } 
    if(! result) {
      result = other.sameAs(indirectionTarget(),trace);
    }
    return result;
  }

  public TypeParameter parameter() {
    return _parameter;
  }

  private void setParameter(TypeParameter parameter) {
    _parameter = parameter;
  }

  private TypeParameter _parameter;

  @Override
  public Declaration declarator() {
    return parameter();
  }

  @Override
  public boolean uniSupertypeOf(Type other, TypeFixer trace) throws LookupException {
    TypeParameter secondParam = parameter();
    if(trace.contains(other,secondParam)) {
      return true;
    }
//    if(other.sameAs(this)) {
//      return true;
//    }
    trace.add(other, secondParam);
    return other.subtypeOf(indirectionTarget(), trace);
  }

  @Override
  public boolean uniSubtypeOf(Type other, TypeFixer trace) throws LookupException {
    TypeParameter firstParam = parameter();
    if(trace.contains(other, firstParam)) {
      return true;
    }
//    if(this.sameAs(other)) {
//      return true;
//    }
    trace.add(other, firstParam);
    return indirectionTarget().subtypeOf(other, trace);
  }
  
  @Override
  public boolean isWildCard() {
      return indirectionTarget().isWildCard();
  }
  
  /**
   * @{inheritDoc}
   */
  @Override
  public boolean contains(Type other, TypeFixer trace) throws LookupException {
    if(trace.contains(other, this)) {
      return true;
    }
    trace.add(other, this);
    return indirectionTarget().contains(other, trace);
  }

  @Override
  public Type lowerBound() throws LookupException {
    return indirectionTarget().lowerBound();
  }

  @Override
  public Type upperBound() throws LookupException {
    return indirectionTarget().upperBound();
  }

    public TypeReference reference() {
      TypeReference result = language(ObjectOrientedLanguage.class).createTypeReference(name());
      result.setUniParent(parameter().parent());
      return result;
    }
}


