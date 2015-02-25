package org.aikodi.chameleon.support.variable;

import java.util.List;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.statement.Statement;
import org.aikodi.chameleon.oo.statement.StatementListContainer;
import org.aikodi.chameleon.oo.statement.StatementListScope;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.oo.variable.RegularVariable;

/**
 * @author Marko van Dooren
 */
public class LocalVariable extends RegularVariable {

  public LocalVariable(String name, TypeReference type, Expression init) {
    super(name, type, init);
  }

  @Override
protected LocalVariable cloneSelf() {
    return new LocalVariable(name(), null,null);
  }

  @Override
public Scope scope() throws LookupException {
    List ancestors = ancestors(StatementListContainer.class);
    return new StatementListScope((StatementListContainer)ancestors.get(ancestors.size() - 1), (Statement)parent());
  }

	@Override
   public LocalVariable actualDeclaration() throws LookupException {
		return this;
	}

}
