package org.aikodi.chameleon.support.expression;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.core.scope.UniversalScope;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Literal;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;

/**
 * @author Marko van Dooren
 */
public class NullLiteral extends Literal {

  public NullLiteral(){
    super("null");
  }

  @Override
protected Type actualType() throws LookupException {
	  return language(ObjectOrientedLanguage.class).getNullType(view().namespace());
  }

  @Override
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
