package be.kuleuven.cs.distrinet.chameleon.support.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.input.ParseException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.test.ModelTest;
import be.kuleuven.cs.distrinet.chameleon.test.provider.ElementProvider;
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
	public ExpressionTest(Project project, ElementProvider<Type> typeProvider) throws ProjectException {
		super(project);
		_typeProvider = typeProvider;
	}
	
  private ElementProvider<Type> _typeProvider;
  
  public ElementProvider<Type> typeProvider() {
  	return _typeProvider;
  }
  
  public int nbThreads() {
	return Runtime.getRuntime().availableProcessors();
  }

//  @Test
//  public void testExpressionTypes() throws Exception {
//	  Collection<Type> types = typeProvider().elements(view());
//	  final BlockingQueue<Type> typeQueue = new ArrayBlockingQueue<Type>(types.size(), true, types);
//	  Action<Type,LookupException> action = createAction();
//		CallableFactory factory = new QueuePollingCallableFactory(action,typeQueue);
//	  new FixedThreadCallableExecutor<LookupException>(factory).run();
//  }
  
  @Test
  public void testExpressionTypes() throws Exception {
	  project().applyToSource(createAction());
//  	for(Type type: typeProvider().elements(view())) {
//  		type.apply(createAction());
//  	}
  }

	protected Action<Type, LookupException> createAction() {
		return new Action<Type,LookupException>(Type.class) {
	  	public void doPerform(Type type) throws LookupException {
	  		processType(type);
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
