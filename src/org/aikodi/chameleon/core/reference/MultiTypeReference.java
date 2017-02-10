package org.aikodi.chameleon.core.reference;

import java.util.Collections;
import java.util.Set;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.TargetDeclaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.MultiTypeSelector;
import org.aikodi.chameleon.util.Util;

import com.google.common.collect.ImmutableSet;

public class MultiTypeReference<D extends Declaration> extends ElementReference<D> {

  protected MultiTypeReference(String fqn, Set<? extends Class<? extends D>> classes, boolean recursiveLimit, Class type) {
    this(createTarget(fqn, classes, recursiveLimit,type), 
        Util.getLastPart(fqn), 
        classes,type);
  }

  protected static <D extends Declaration> MultiTypeReference<D> createTarget(String fqn, Set classes, boolean recursiveLimit, Class type) {
    String allButLastPart = Util.getAllButLastPart(fqn);
    if(allButLastPart == null) {
      return null;
    } else {
      Set cls = recursiveLimit ? classes : Collections.singleton(Declaration.class);
      return new MultiTypeReference<D>(allButLastPart, cls, recursiveLimit, type);
    }
  }


  public MultiTypeReference(CrossReferenceTarget target, String name, Set<? extends Class<? extends D>> classes, Class<D> type) {
    super(name, type);
    setTarget(target); 
    _classes = ImmutableSet.copyOf(classes);
  }

  public MultiTypeReference(String fqn, Set classes, Class<D> type, Class recursiveType) {
    this(createTarget(fqn, classes, false,recursiveType), 
        Util.getLastPart(fqn),classes,type);
  }

  @Override
  public DeclarationSelector<D> selector() {
    return new MultiTypeSelector<D>(_classes) {

      @Override
      public String name() {
        return MultiTypeReference.this.name();
      }
    };
  }

  @Override
  protected Element cloneSelf() {
    return new MultiTypeReference<D>(null,name(),_classes,referencedType());
  }

  private Set<Class<? extends D>> _classes;

}
