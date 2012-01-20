package chameleon.support.member.simplename.operator.postfix;




import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.reference.CrossReferenceTarget;
import chameleon.support.member.simplename.SimpleNameMethodInvocation;


/**
 * @author Marko van Dooren
 */
public class PostfixOperatorInvocation extends SimpleNameMethodInvocation<PostfixOperatorInvocation,PostfixOperator> {

	/**
	 * @param target
	 * @param name
	 */
 /*@
   @ public behavior
   @
   @ pre target != null; 
   @*/
	public PostfixOperatorInvocation(String name, CrossReferenceTarget target) {
		super(target, name);
	}

  protected PostfixOperatorInvocation cloneInvocation(CrossReferenceTarget target) {
    return new PostfixOperatorInvocation(name(), target);
  }
  
  @Override
  public DeclarationSelector<PostfixOperator> createSelector() {
    return new SimpleNameMethodSelector() {
      @Override
      public Class<PostfixOperator> selectedClass() {
        return PostfixOperator.class;
      }

    };
  }

}
