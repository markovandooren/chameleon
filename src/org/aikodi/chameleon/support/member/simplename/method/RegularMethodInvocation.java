package org.aikodi.chameleon.support.member.simplename.method;


import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.support.member.simplename.SimpleNameMethodInvocation;


/**
 * @author Marko van Dooren
 */
public class RegularMethodInvocation extends SimpleNameMethodInvocation<NormalMethod> {

  public RegularMethodInvocation(String name, CrossReferenceTarget target) {
    super(target, name);
  }
  
  @Override
  protected DeclarationSelector<NormalMethod> createSelector() throws LookupException {
  	return new SimpleNameMethodSelector() {
      @Override
      public Class<NormalMethod> selectedClass() {
        return NormalMethod.class;
      }

    };
  }

  /********
   * MISC *
   ********/
  
  @Override
protected RegularMethodInvocation cloneSelf() {
    return new RegularMethodInvocation(name(), null);
  }

  /**
   * @{inheritDoc}
   */
  @Override
  public Class<NormalMethod> referencedType() {
    return NormalMethod.class;
  }
}
