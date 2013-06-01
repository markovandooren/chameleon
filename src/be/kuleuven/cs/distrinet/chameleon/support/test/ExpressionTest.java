package be.kuleuven.cs.distrinet.chameleon.support.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Test;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
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
	 * @param provider
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
	public ExpressionTest(Project provider, ElementProvider<Type> typeProvider) throws ProjectException {
		super(provider);
		_typeProvider = typeProvider;
	}
	
  private ElementProvider<Type> _typeProvider;
  
  public ElementProvider<Type> typeProvider() {
  	return _typeProvider;
  }
  
  public int nbThreads() {
	return Runtime.getRuntime().availableProcessors();
  }

  @Test
  public void testExpressionTypes() throws Exception {
	  Collection<Type> types = typeProvider().elements(view());
	  final BlockingQueue<Type> typeQueue = new ArrayBlockingQueue<Type>(types.size(), true, types);
	  CallableFactory factory = new QueuePollingCallableFactory(new Action<Type,LookupException>(Type.class) {
	  	public void perform(Type type) throws LookupException {
	  		processType(type);
	  	} 
	  },typeQueue);
	  new FixedThreadCallableExecutor<LookupException>(factory).run();
  }
  
   public void processType(Type type) throws LookupException {
  	 List<Expression> exprs = type.descendants(Expression.class);
  	 for(Expression expression : exprs) {
  		 Type expressionType = expression.getType();
  		 assertTrue(expressionType != null);
  	 }
   }

}
