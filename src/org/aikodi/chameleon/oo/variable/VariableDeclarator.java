package org.aikodi.chameleon.oo.variable;

import java.util.List;

import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.core.variable.Variable;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.type.TypeReference;

public interface VariableDeclarator extends Element, DeclarationContainer {

	public List<VariableDeclaration> variableDeclarations();

	public Variable createVariable(String name, Expression expression);
	
	public TypeReference typeReference();
	
	public List<Modifier> modifiers();

//	public Type type() throws LookupException;
}
