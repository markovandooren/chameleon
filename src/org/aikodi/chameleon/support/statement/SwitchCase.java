package org.aikodi.chameleon.support.statement;

import java.util.List;

import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.statement.ExceptionSource;
import org.aikodi.chameleon.oo.statement.Statement;
import org.aikodi.chameleon.oo.statement.StatementListContainer;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.chameleon.util.association.Single;

import be.kuleuven.cs.distrinet.rejuse.association.OrderedMultiAssociation;

/**
 * @author Marko van Dooren
 */
public class SwitchCase extends ElementImpl implements StatementListContainer, ExceptionSource {

  protected SwitchCase() {
  }

  public SwitchCase(SwitchLabel label) {
    setLabel(label);
  }

  /**
   * STATEMENTS
   */
  private Multi<Statement> _statements = new Multi<Statement>(this);

  public OrderedMultiAssociation getStatementsLink() {
    return _statements;
  }

  public void addStatement(Statement statement) {
    add(_statements, statement);
  }

  public void removeStatement(Statement statement) {
    remove(_statements, statement);
  }

  @Override
  public List<Statement> statements() {
    return _statements.getOtherEnds();
  }

  /**
   * LABELS
   */
  private Single<SwitchLabel> _labels = new Single<SwitchLabel>(this);

  public void setLabel(SwitchLabel label) {
    set(_labels, label);
  }

  public SwitchLabel getLabel() {
    return _labels.getOtherEnd();
  }

  /**
   * @return
   */
  @Override
  protected SwitchCase cloneSelf() {
    return new SwitchCase();
  }

  @Override
  public int getIndexOf(Statement statement) {
    return statements().indexOf(statement) + 1;
  }

  @Override
  public List<Statement> statementsAfter(Statement statement) {
    List<Statement> statements = statements();
    int index = statements.indexOf(statement);
    // returns a view on a clone of _statements (getStatements() clones the
    // list).
    // the view depends on the local variable, but since no other references
    // exist
    // this is not a problem.
    return statements.subList(index, statements.size());
  }

  @Override
  public Verification verifySelf() {
    return checkNull(getLabel(), "The label is missing", Valid.create());
  }

}
