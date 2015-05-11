package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.plugin.ObjectOrientedFactory;
import org.aikodi.chameleon.oo.type.Type;

public class CapturedTypeParameter extends FormalTypeParameter {
  //FIXME a captured type parameter should NOT be a formal type parameter but an instantiated type parameter!!!!
  //      I must modify the instantiated type parameter hierarchy.
  public CapturedTypeParameter(String name) {
    super(name);
  }

  @Override
  protected CapturedTypeParameter cloneSelf() {
    return new CapturedTypeParameter(name());
  }

  public Type resolveForRoundTrip() throws LookupException {
    Type result = language().plugin(ObjectOrientedFactory.class).createLazyCapturedTypeVariable(name(),this);
    result.setUniParent(parent());
    return result;
  }

  @Override
  protected synchronized Type createSelectionType() throws LookupException {
    if(_selectionTypeCache == null) {
      _selectionTypeCache = language().plugin(ObjectOrientedFactory.class).createEagerCapturedTypeVariable(name(),upperBound(),this);
    }
    return _selectionTypeCache;
  }

  @Override
  public synchronized void flushLocalCache() {
    super.flushLocalCache();
    _selectionTypeCache = null;
  }

  private Type _selectionTypeCache;



}
