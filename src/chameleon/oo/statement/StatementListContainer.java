package chameleon.oo.statement;

import java.util.List;

import chameleon.core.element.Element;

/**
 * 
 * @author Marko van Dooren
 */
public interface StatementListContainer extends Element {

    public int getIndexOf(Statement statement);
    
    public List<Statement> statements();
    
    public List<Statement> statementsAfter(Statement statement);
}
