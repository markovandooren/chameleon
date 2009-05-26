package chameleon.core.statement;

import java.util.List;

import chameleon.core.element.Element;

/**
 * 
 * @author Marko van Dooren
 *
 * @param <E>
 * @param <P>
 */
public interface StatementListContainer<E extends Element, P extends Element> {

    public int getIndexOf(Statement statement);
    
    public List<Statement> getStatements();
    
    public List<Statement> statementsAfter(Statement statement);
}
