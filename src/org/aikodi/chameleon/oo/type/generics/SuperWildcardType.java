package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeFixer;
import org.aikodi.chameleon.oo.type.TypeReference;

public class SuperWildcardType extends IntervalType {

  public SuperWildcardType(Type lowerBound) throws LookupException {
    super("? super "+lowerBound.name(), lowerBound, lowerBound.language(ObjectOrientedLanguage.class).getDefaultSuperClass(lowerBound.view().namespace()));
  }



  protected SuperWildcardType(String name, Type lowerBound, Type upperBound) {
    super(name, lowerBound, upperBound);
  }

  public Type bound() {
    return lowerBound();
  }

  @Override
  protected Element cloneSelf() {
    return new SuperWildcardType(name(), lowerBound(), upperBound());
  }

  @Override
  public String getFullyQualifiedName() {
    return "? super "+lowerBound().getFullyQualifiedName();
  }

  /**
   * @{inheritDoc}
   */
  @Override
  public boolean contains(Type other, TypeFixer trace) throws LookupException {
    return lowerBound().subtypeOf(other,trace);
  }
  
  /**
   * @{inheritDoc}
   */
  @Override
  public boolean uniSupertypeOf(Type other, TypeFixer trace) throws LookupException {
    return lowerBound().subtypeOf(other.lowerBound(),trace);
  }

    public TypeReference reference() {
    ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
      TypeReference reference = language.reference(bound());
      Element parent = reference.lexical().parent();
      reference.setUniParent(null);
      TypeReference result = language.createSuperReference(reference);
      result.setUniParent(parent);
      return result;
    }
}
