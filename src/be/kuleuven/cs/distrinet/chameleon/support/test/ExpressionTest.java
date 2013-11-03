package be.kuleuven.cs.distrinet.chameleon.support.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import java.io.IOException;

import org.junit.Test;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.input.ParseException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.test.ModelTest;
import be.kuleuven.cs.distrinet.chameleon.test.provider.ElementProvider;
import be.kuleuven.cs.distrinet.chameleon.util.concurrent.CallableFactory;
import be.kuleuven.cs.distrinet.chameleon.util.concurrent.FixedThreadCallableExecutor;
import be.kuleuven.cs.distrinet.chameleon.util.concurrent.QueuePollingCallableFactory;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.chameleon.workspace.ProjectException;
import be.kuleuven.cs.distrinet.rejuse.action.Action;

/**
 * @author Marko van Dooren
 */
public class ExpressionTest extends ModelTest {
  
	/**
	 * Create a new expression tester
	 * @param project
	 * @throws IOException 
	 * @throws ParseException 
	 */
 /*@
   @ public behavior
   @
   @ post modelProvider() == modelProvider;
   @ post typeProvider() == typeProvider;
   @ post baseRecursive();
   @ post customRecursive();
   @*/
	public ExpressionTest(Project project, ElementProvider<Namespace> typeProvider, ExecutorService executor) throws ProjectException {
		super(project);
		_typeProvider = typeProvider;
		threadPool = executor;
	}
	
  private ElementProvider<Namespace> _typeProvider;
  
  public ElementProvider<Namespace> typeProvider() {
  	return _typeProvider;
  }
  
  public int nbThreads() {
	return Runtime.getRuntime().availableProcessors();
  }

  @Test
  public void testExpressionTypes() throws Exception {
//	  Collection<Type> types = typeProvider().elements(view());
//	  final BlockingQueue<Type> typeQueue = new ArrayBlockingQueue<Type>(types.size(), true, types);
//	  Action<Type,LookupException> action = createAction();
//		CallableFactory factory = new QueuePollingCallableFactory(action,typeQueue);
//	  new FixedThreadCallableExecutor<LookupException>(factory,threadPool).run();
	  Collection<Namespace> typess = typeProvider().elements(view());
	  Collection<Namespace> namespaces = new ArrayList<>();
	  for(Namespace ns: typess) {
	  	namespaces.addAll(ns.getAllSubNamespaces());
	  }
	  final BlockingQueue<Namespace> typeQueue = new ArrayBlockingQueue<Namespace>(namespaces.size(), true, namespaces);
	  Action<Namespace,LookupException> action = createAction();
		CallableFactory factory = new QueuePollingCallableFactory(action,typeQueue);
	  new FixedThreadCallableExecutor<LookupException>(factory,threadPool).run();
  }
  
  private ExecutorService threadPool;
  
//  @Test
//  public void testExpressionTypes() throws Exception {
//	  project().applyToSource(createAction());
////  	for(Type type: typeProvider().elements(view())) {
////  		type.apply(createAction());
////  	}
//  }

	protected Action<Namespace, LookupException> createAction() {
		return new Action<Namespace,LookupException>(Namespace.class) {
	  	public void doPerform(Namespace ns) throws LookupException {
	  		for(NamespaceDeclaration nsp: ns.getNamespaceParts()) {
	  			for(Type type: nsp.descendants(Type.class)) {
	  				processType(type);
	  			}
				}
	  	} 
	  };
	}
  
//	private int _count;
   public void processType(Type type) throws LookupException {
//  	 System.out.println(++_count + " checking expression types of: "+type);
  	 List<Expression> exprs = type.descendants(Expression.class);
  	 for(Expression expression : exprs) {
  		 Type expressionType = expression.getType();
  		 assertTrue(expressionType != null);
  	 }
   }

}
