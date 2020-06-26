package org.aikodi.chameleon.support.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.input.ParseException;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.test.ModelTest;
import org.aikodi.chameleon.test.provider.ElementProvider;
import org.aikodi.chameleon.util.concurrent.CallableFactory;
import org.aikodi.chameleon.util.concurrent.FixedThreadCallableExecutor;
import org.aikodi.chameleon.util.concurrent.QueuePollingCallableFactory;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.chameleon.workspace.ProjectException;
import org.aikodi.rejuse.action.UniversalConsumer;
import org.junit.Test;

/**
 * @author Marko van Dooren
 */
public class ExpressionTest extends ModelTest {

	/**
	 * Create a new expression tester
	 * 
	 * @param project The project containing the types to be tested.
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public ExpressionTest(Project project, ElementProvider<Namespace> typeProvider, ExecutorService executor)
			throws ProjectException {
		super(project);
		_typeProvider = typeProvider;
		threadPool = executor;
	}

	private ElementProvider<Namespace> _typeProvider;

	public ElementProvider<Namespace> typeProvider() {
		return _typeProvider;
	}

	@Test
	public void testExpressionTypes() throws Exception {
		Collection<Namespace> types = typeProvider().elements(view());
		Collection<Namespace> namespaces = new ArrayList<>();
		for (Namespace ns : types) {
			namespaces.addAll(ns.descendantNamespaces());
		}
		final BlockingQueue<Namespace> typeQueue = new ArrayBlockingQueue<Namespace>(namespaces.size(), true,
				namespaces);
		UniversalConsumer<Namespace, LookupException> action = createAction();
		CallableFactory factory = new QueuePollingCallableFactory(action, typeQueue);
		new FixedThreadCallableExecutor<LookupException>(factory, threadPool).run();
	}

	private ExecutorService threadPool;

	protected UniversalConsumer<Namespace, LookupException> createAction() {
		return new UniversalConsumer<Namespace, LookupException>(Namespace.class) {
			@Override
			public void accept(Namespace ns) throws LookupException {
				for (NamespaceDeclaration nsp : ns.namespaceDeclarations()) {
					for (Type type : nsp.lexical().descendants(Type.class)) {
						processType(type);
					}
				}
			}
		};
	}

	// private int _count;
	public void processType(Type type) throws LookupException {
		// System.out.println(++_count + " checking expression types of: "+type);
		List<Expression> exprs = type.lexical().descendants(Expression.class);
		for (Expression expression : exprs) {
			Type expressionType = expression.getType();
			assertTrue(expressionType != null);
		}
	}

}
