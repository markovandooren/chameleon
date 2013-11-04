package be.kuleuven.cs.distrinet.chameleon.oo.variable;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.Modifier;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;

public interface VariableDeclarator extends Element, DeclarationContainer {

	public List<VariableDeclaration> variableDeclarations();

	public Variable createVariable(String name, Expression expression);
	
	public TypeReference typeReference();
	
	public List<Modifier> modifiers();

//	public Type type() throws LookupException;
}
