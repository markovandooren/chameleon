package be.kuleuven.cs.distrinet.chameleon.aspect.oo.weave.infrastructure;

import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.ProgrammingAdvice;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.modifier.Throwing;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.language.AspectOrientedOOLanguage;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.NamedTargetExpression;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Block;
import be.kuleuven.cs.distrinet.chameleon.oo.type.BasicTypeReference;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.FormalParameter;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.VariableDeclaration;
import be.kuleuven.cs.distrinet.chameleon.support.expression.ClassCastExpression;
import be.kuleuven.cs.distrinet.chameleon.support.expression.InstanceofExpression;
import be.kuleuven.cs.distrinet.chameleon.support.statement.CatchClause;
import be.kuleuven.cs.distrinet.chameleon.support.statement.IfThenElseStatement;
import be.kuleuven.cs.distrinet.chameleon.support.statement.ThrowStatement;
import be.kuleuven.cs.distrinet.chameleon.support.variable.LocalVariable;
import be.kuleuven.cs.distrinet.chameleon.support.variable.LocalVariableDeclarator;

public class AfterThrowingExpressionFactory extends AdvisedExpressionFactory {

	public AfterThrowingExpressionFactory(ExpressionInfrastructureFactory factory) {
		super(factory);
	}

	public CatchClause clause(String name, Type caughtType) {
		Block catchBlockBody = new Block();
		ThrowStatement rethrow = new ThrowStatement(new NamedTargetExpression(name));
		try {
			ProgrammingAdvice advice = factory().getAdvice();
			Throwing m = (Throwing) advice.modifiers(advice.language(AspectOrientedOOLanguage.class).THROWING()).get(0);
			// Do a type check if there is a type defined
			if (m.hasParameter()) {
				Type declaredType = m.parameter().getType();
				// If the declared type is the same or a super type of the type caught, add the advice and expose the parameter
				if (caughtType.assignableTo(declaredType)) {
					LocalVariableDeclarator paramExpose = new LocalVariableDeclarator(m.parameter().getTypeReference().clone());
					paramExpose.add(new VariableDeclaration(m.parameter().getName(), new NamedTargetExpression(name)));
					catchBlockBody.addStatement(paramExpose);
					catchBlockBody.addBlock(advice.body().clone());
					catchBlockBody.addStatement(rethrow);
				}
				// If the declared type is a subtype of the type caught, we need a runtime check
				else if (declaredType.subTypeOf(caughtType)) {
					Block innerBody = new Block();
					LocalVariableDeclarator paramExpose = new LocalVariableDeclarator(m.parameter().getTypeReference().clone());
					paramExpose.add(new VariableDeclaration(m.parameter().getName(), new ClassCastExpression(m.parameter().getTypeReference().clone(), new NamedTargetExpression(name))));

					innerBody.addStatement(paramExpose);
					innerBody.addBlock(advice.body().clone());

					InstanceofExpression instanceOf = new InstanceofExpression(new NamedTargetExpression(name), new BasicTypeReference(declaredType.getFullyQualifiedName()));
					IfThenElseStatement runtimeTest = new IfThenElseStatement(instanceOf, innerBody, null);

					catchBlockBody.addStatement(runtimeTest);
					catchBlockBody.addStatement(rethrow);
				}
				else {
					// The types aren't related, so just re throw the exception
					catchBlockBody.addStatement(rethrow);
				}
			} else {
				catchBlockBody.addBlock(advice.body().clone());
				catchBlockBody.addStatement(rethrow);
			}
		} catch (ModelException e) {
			throw new ChameleonProgrammerException(e);
		}
		CatchClause catchClause = new CatchClause(new FormalParameter(name, new BasicTypeReference(caughtType.getFullyQualifiedName())), catchBlockBody);
		return catchClause;
	}
}
