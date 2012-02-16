package chameleon.oo.variable;

import java.util.List;

import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.modifier.Modifier;
import chameleon.oo.expression.Expression;
import chameleon.oo.type.TypeReference;

public interface VariableDeclarator extends Element, DeclarationContainer {

	public List<VariableDeclaration> variableDeclarations();

	public Variable createVariable(SimpleNameSignature signature, Expression expression);
	
	public TypeReference typeReference();
	
	public List<Modifier> modifiers();

//	public Type type() throws LookupException;
}
