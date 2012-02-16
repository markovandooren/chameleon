package chameleon.support.member.simplename.method;


import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReferenceTarget;
import chameleon.support.member.simplename.SimpleNameMethodInvocation;


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
  
  protected RegularMethodInvocation cloneInvocation(CrossReferenceTarget target) {
  	//target is already cloned.
    return new RegularMethodInvocation(name(), target);
  }


}
