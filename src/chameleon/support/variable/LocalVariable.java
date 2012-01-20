package chameleon.support.variable;

import java.util.List;

import org.rejuse.predicate.TypePredicate;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.scope.Scope;
import chameleon.oo.expression.Expression;
import chameleon.oo.statement.Statement;
import chameleon.oo.statement.StatementListContainer;
import chameleon.oo.statement.StatementListScope;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.oo.variable.RegularVariable;
import chameleon.oo.variable.VariableContainer;

/**
 * @author Marko van Dooren
 */
public class LocalVariable extends RegularVariable<LocalVariable> {

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
