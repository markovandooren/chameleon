package be.kuleuven.cs.distrinet.chameleon.aspect.oo.weave.factory;

import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.plugin.LanguagePlugin;

public interface OOFactory extends LanguagePlugin {

	public Statement createReturn(Expression expression);
	
	public Statement createTryFinally(Statement tr, Statement fin);
	
}
