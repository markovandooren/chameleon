package org.aikodi.chameleon.aspect.oo.weave.factory;

import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.statement.Statement;
import org.aikodi.chameleon.plugin.LanguagePlugin;

public interface OOFactory extends LanguagePlugin {

	public Statement createReturn(Expression expression);
	
	public Statement createTryFinally(Statement tr, Statement fin);
	
}
