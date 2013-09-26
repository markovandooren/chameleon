package be.kuleuven.cs.distrinet.chameleon.support.member.simplename.operator.postfix;




import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReferenceTarget;
import be.kuleuven.cs.distrinet.chameleon.support.member.simplename.SimpleNameMethodInvocation;
import be.kuleuven.cs.distrinet.chameleon.support.member.simplename.operator.infix.InfixOperator;
import be.kuleuven.cs.distrinet.chameleon.util.profile.Timer;


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

//  @Override
//  public PostfixOperator getElement() throws LookupException {
//  	Timer.POSTFIX_OPERATOR_INVOCATION.start();
//  	PostfixOperator result = super.getElement();
//  	Timer.POSTFIX_OPERATOR_INVOCATION.stop();
//  	return result;
//  }

}
