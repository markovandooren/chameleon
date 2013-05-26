package be.kuleuven.cs.distrinet.chameleon.support.expression;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.UniversalScope;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Literal;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;

/**
 * @author Marko van Dooren
 */
public class NullLiteral extends Literal {

  public NullLiteral(){
    super("null");
  }

  protected Type actualType() throws LookupException {
	  return language(ObjectOrientedLanguage.class).getNullType(view().namespace());
  }

  protected NullLiteral cloneSelf() {
    return new NullLiteral();
  }

  public Scope getAccessibilityDomain() throws LookupException {
    return new UniversalScope();
  }

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

}
