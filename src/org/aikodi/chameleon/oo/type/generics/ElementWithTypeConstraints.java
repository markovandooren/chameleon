package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.view.ObjectOrientedView;
import org.aikodi.rejuse.function.BiFunction;
import org.aikodi.rejuse.function.Function;

import java.util.List;
import java.util.function.Supplier;

public interface ElementWithTypeConstraints extends ElementWithTypeBounds {
  
  public default Type upperBound() throws LookupException {
    final Supplier<Type>defaultValue = () -> view(ObjectOrientedView.class).topLevelType();
    return combine(defaultValue, c -> c.upperBound(), (t1,t2) -> t1.intersection(t2));
  }

  public default Type lowerBound() throws LookupException {
    final Supplier<Type>defaultValue = () -> language(ObjectOrientedLanguage.class).getNullType(view().namespace());
    return combine(defaultValue, c -> c.lowerBound(), (t1,t2) -> t1.union(t2));
  }

  public default Type combine(Supplier<Type> defaultValue, 
      Function<TypeConstraint,Type,LookupException> boundSelector, 
      BiFunction<Type,Type,Type,LookupException> combiner) throws LookupException {
    List<TypeConstraint> constraints = constraints();
    Type result;
    int size = constraints.size();
    if(size == 0) {
      result = defaultValue.get();
    } else {
      result = boundSelector.apply(constraints.get(0));
      for(int i = 1; i < size; i++) {
        result = combiner.apply(result, boundSelector.apply(constraints.get(i)));
      }
    }
    return result;
  }

  public List<TypeConstraint> constraints();
}
