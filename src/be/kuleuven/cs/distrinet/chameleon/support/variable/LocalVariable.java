package be.kuleuven.cs.distrinet.chameleon.support.variable;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.StatementListContainer;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.StatementListScope;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.RegularVariable;

/**
 * @author Marko van Dooren
 */
public class LocalVariable extends RegularVariable {

  public LocalVariable(String name, TypeReference type, Expression init) {
    super(name, type, init);
  }

  protected LocalVariable cloneSelf() {
    return new LocalVariable(name(), null,null);
  }

  public Scope scope() throws LookupException {
    List ancestors = ancestors(StatementListContainer.class);
    return new StatementListScope((StatementListContainer)ancestors.get(ancestors.size() - 1), (Statement)parent());
  }

	public LocalVariable actualDeclaration() throws LookupException {
		return this;
	}

}
