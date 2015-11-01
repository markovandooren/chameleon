package org.aikodi.chameleon.support.member.simplename.operator.postfix;




import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.support.member.simplename.SimpleNameMethodInvocation;
import org.aikodi.chameleon.support.member.simplename.operator.infix.InfixOperator;


/**
 * @author Marko van Dooren
 */
public class PostfixOperatorInvocation extends SimpleNameMethodInvocation<PostfixOperator> {

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

  @Override
protected PostfixOperatorInvocation cloneSelf() {
    return new PostfixOperatorInvocation(name(), null);
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

  /**
   * @{inheritDoc}
   */
  @Override
  public Class<PostfixOperator> referencedType() {
    return PostfixOperator.class;
  }

}
