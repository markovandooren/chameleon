package be.kuleuven.cs.distrinet.chameleon.support.member.simplename.method;


import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReferenceTarget;
import be.kuleuven.cs.distrinet.chameleon.support.member.simplename.SimpleNameMethodInvocation;


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


}
