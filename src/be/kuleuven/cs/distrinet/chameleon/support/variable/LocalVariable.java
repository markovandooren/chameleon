package be.kuleuven.cs.distrinet.chameleon.support.variable;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.StatementListContainer;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.StatementListScope;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.RegularVariable;
import be.kuleuven.cs.distrinet.rejuse.predicate.TypePredicate;

/**
 * @author Marko van Dooren
 */
public class LocalVariable extends RegularVariable {

  public LocalVariable(SimpleNameSignature sig, TypeReference type, Expression init) {
    super(sig, type, init);
  }

  protected LocalVariable cloneThis() {
    Expression expr = null;
    if(getInitialization() != null) {
      expr = getInitialization().clone();
    }
    return new LocalVariable(signature().clone(), (TypeReference)getTypeReference().clone(), expr);
  }

  public Scope scope() throws LookupException {
    List ancestors = ancestors();
    new TypePredicate(StatementListContainer.class).filter(ancestors);
    return new StatementListScope((StatementListContainer)ancestors.get(ancestors.size() - 1), (Statement)parent());
  }

	public LocalVariable actualDeclaration() throws LookupException {
		return this;
	}

}
