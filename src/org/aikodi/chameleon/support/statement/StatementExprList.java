package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.statement.ExceptionSource;
import org.aikodi.chameleon.oo.statement.Statement;
import org.aikodi.chameleon.util.association.Multi;

import java.util.Collections;
import java.util.List;

/**
 * A list of statement expressions as used in the initialization clause of a for
 * statement. It contains a list of statement expressions.
 * 
 * @author Marko van Dooren
 */
public class StatementExprList extends ElementImpl implements ForInit, ExceptionSource {

  public StatementExprList() {
  }

  /**
   * STATEMENT EXPRESSIONS
   */
  private Multi<StatementExpression> _statementExpressions = new Multi<StatementExpression>(this);

  public void addStatement(StatementExpression statement) {
    add(_statementExpressions, statement);
  }

  public void removeStatement(StatementExpression statement) {
    remove(_statementExpressions, statement);
  }

  public List<StatementExpression> statements() {
    return _statementExpressions.getOtherEnds();
  }

  @Override
  public StatementExprList cloneSelf() {
    return new StatementExprList();
  }

  public int getIndexOf(Statement statement) {
    return statements().indexOf(statement) + 1;
  }

  public int getNbStatements() {
    return statements().size();
  }

  @Override
  public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
    return declarations();
  }

  @Override
  public List<? extends Declaration> declarations() throws LookupException {
    return Collections.EMPTY_LIST;
  }

  @Override
  public LookupContext localContext() throws LookupException {
    return language().lookupFactory().createLocalLookupStrategy(this);
  }

  @Override
  public <D extends Declaration> List<? extends SelectionResult<D>> declarations(DeclarationSelector<D> selector)
      throws LookupException {
    return Collections.emptyList();
  }

  @Override
  public Verification verifySelf() {
    return Valid.create();
  }

}
