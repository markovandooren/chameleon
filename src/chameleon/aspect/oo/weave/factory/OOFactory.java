package chameleon.aspect.oo.weave.factory;

import chameleon.oo.expression.Expression;
import chameleon.oo.statement.Statement;
import chameleon.plugin.Plugin;

public interface OOFactory extends Plugin {

	public Statement createReturn(Expression expression);
	
	public Statement createTryFinally(Statement tr, Statement fin);
	
}
