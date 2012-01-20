package chameleon.aspect.oo.model.advice.weave.transform.runtime;

import java.util.ArrayList;
import java.util.List;

import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.core.weave.JoinPointWeaver;
import chameleon.aspect.core.weave.registry.NamingRegistry;
import chameleon.aspect.oo.model.advice.weave.transform.runtime.transformationprovider.RuntimeExpressionFactory;
import chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.expression.Expression;
import chameleon.oo.expression.NamedTargetExpression;
import chameleon.oo.statement.Block;
import chameleon.oo.statement.Statement;
import chameleon.oo.type.BasicTypeReference;
import chameleon.oo.variable.VariableDeclaration;
import chameleon.support.member.simplename.operator.prefix.PrefixOperatorInvocation;
import chameleon.support.statement.IfThenElseStatement;
import chameleon.support.variable.LocalVariableDeclarator;

/**
 * 	See the Coordinator interface for documentation. This class implements some basic functionality
 * 
 * @author Jens
 *
 * @param <T>
 */
public abstract class AbstractCoordinator<T extends Element<?>> implements Coordinator<T> {

	/**
	 * 	The advice transformer used
	 * 
	 * 	FIXME: changed this type, rename the variables/getter/setter
	 */
	private RuntimeTransformationProvider adviceTransformationProvider;
	
	/**
	 * 	The matched joinpoint
	 */
	private MatchResult<? extends Element> matchResult;
	
	/**
	 * 	Constructor
	 * 
	 * 	@param 	adviceTransformationProvider
	 * 			The used advice transformer
	 * 	@param 	matchResult
	 * 			The join point
	 */
	public AbstractCoordinator(RuntimeTransformationProvider adviceTransformationProvider, MatchResult<? extends Element> matchResult) {
		this.adviceTransformationProvider = adviceTransformationProvider;
		this.matchResult = matchResult;
	}
	
	/**
	 * 	Get the join point
	 * 
	 * 	@return	the join point
	 */
	public MatchResult<? extends Element> getMatchResult() {
		return matchResult;
	}
	
	/**
	 * 	Get a list of declarations for all runtime tests in a pointcut expression tree. 
	 * 
	 * 	For instance, a if(Logger.enabled) test could be: boolean $_randomname = Logger.enabled;
	 * 		
	 * 
	 * 	@param 	tree
	 * 			The pointcut expression tree
	 * 	@param 	expressionNames
	 * 			The naming registry for expressions
	 * 	@return	The list of statements
	 * @throws LookupException 
	 */
	protected List<Statement> getDeclarations(RuntimePointcutExpression tree, NamingRegistry<RuntimePointcutExpression> expressionNames) throws LookupException {
		List<Statement> result = new ArrayList<Statement>();
		
		if (tree == null)
			return result;

		// Get the tree in list-form, with the leaves first (postorder)
		List<RuntimePointcutExpression> expressions = tree.toPostorderList();

		// Get their expressions and assign them to booleans
		for (RuntimePointcutExpression expression : expressions) {
			RuntimePointcutExpression<?,?> actualExpression = (RuntimePointcutExpression<?,?>) expression.origin();
			
			RuntimeExpressionFactory transformer = getAdviceTransformationProvider().getRuntimeExpressionProvider(actualExpression);
			Expression runtimeCheck = transformer.getExpression(actualExpression, expressionNames);
			
			// Create a boolean to assign the result to
			LocalVariableDeclarator testDecl = new LocalVariableDeclarator(new BasicTypeReference("boolean"));
			VariableDeclaration test = new VariableDeclaration("_$" + expressionNames.getName(actualExpression));
			test.setInitialization(runtimeCheck);
			testDecl.add(test);
			
			result.add(testDecl);
		}
		
		return result;
	}
	
	
	/**
	 * 	Return the test corresponding to the items in the tree
	 * 
	 * 	@param 	tree
	 * 			The expression tree
	 * 	@param 	expressionNames
	 * 			The naming registry for expressions
	 * @throws LookupException 
	 */
	protected Block addTest(RuntimePointcutExpression tree, NamingRegistry<RuntimePointcutExpression> expressionNames) throws LookupException {
		return addTest(tree, expressionNames, getTest(tree, expressionNames));
	}
	
	protected Block addTest(RuntimePointcutExpression tree, NamingRegistry<RuntimePointcutExpression> expressionNames, IfThenElseStatement test) throws LookupException {
		Block body = new Block();
		
		// Insert all the selected runtime expression-statements
		List<Statement> statements = getDeclarations(tree, expressionNames);
		for (Statement st : statements)
			body.addStatement(st);
		
		body.addStatement(test);
		
		return body;
	}
	
	protected IfThenElseStatement getTest(RuntimePointcutExpression<?,?> tree, NamingRegistry<RuntimePointcutExpression> expressionNames) throws LookupException {
		// Now, convert the actual expression to the right test - get the name of the root
		Expression completeTest = new NamedTargetExpression("_$" + expressionNames.getName((RuntimePointcutExpression<?,?>) tree.origin()));
		
		// Negate it, since we do the return of the original method if the expression returns false
		PrefixOperatorInvocation negation = new PrefixOperatorInvocation("!", completeTest);
		
		IfThenElseStatement ifStatement = new IfThenElseStatement(negation, getTerminateBody(), null);
		
		return ifStatement;
	}
	
	/**
	 * 	Get the body of the if-clause that determines a runtime check failed.
	 * 
	 * 	E.g. for method invocations, this executes the original method (instead of weaving)
	 * 
	 * 	@return	The body of the if clause
	 * @throws LookupException 
	 */
	protected abstract Statement getTerminateBody() throws LookupException;
	

	/**
	 * 	Return the advice transformer
	 * 
	 * 	@return	The advice transformer
	 */
	public RuntimeTransformationProvider getAdviceTransformationProvider() {
		return adviceTransformationProvider;
	}

	public JoinPointWeaver getNextWeavingEncapsulator() {
		return adviceTransformationProvider.joinPointWeaver().next();
	}
}
