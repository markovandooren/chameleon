package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.oo.type.Type;

public class ConstrainedType extends IntervalType {

  public ConstrainedType(Type lowerBound, Type upperBound) {
    super("? super "+lowerBound.name()+" extends "+upperBound.name(), lowerBound, upperBound);
  }
  
  @Override
  public ConstrainedType cloneSelf() {
    return new ConstrainedType(lowerBound(), upperBound());
  }
  
  @Override
  public String getFullyQualifiedName() {
    return "? super "+lowerBound().getFullyQualifiedName()+" extends "+upperBound().getFullyQualifiedName();
  }

}
