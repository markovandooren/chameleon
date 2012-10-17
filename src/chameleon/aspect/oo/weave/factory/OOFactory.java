package chameleon.aspect.oo.weave.factory;

import chameleon.oo.expression.Expression;
import chameleon.oo.statement.Statement;
import chameleon.plugin.LanguagePlugin;

public interface OOFactory extends LanguagePlugin {

	public Statement createReturn(Expression expression);
	
	public Statement createTryFinally(Statement tr, Statement fin);
	
}
