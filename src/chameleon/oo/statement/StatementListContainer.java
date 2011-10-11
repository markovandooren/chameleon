package chameleon.oo.statement;

import java.util.List;

import chameleon.core.element.Element;

/**
 * 
 * @author Marko van Dooren
 *
 * @param <E>
 * @param <P>
 */
public interface StatementListContainer<E extends Element> extends Element<E>{

    public int getIndexOf(Statement statement);
    
    public List<Statement> statements();
    
    public List<Statement> statementsAfter(Statement statement);
}
