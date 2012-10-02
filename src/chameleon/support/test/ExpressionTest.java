package chameleon.support.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

import chameleon.core.lookup.LookupException;
import chameleon.input.ParseException;
import chameleon.oo.expression.Expression;
import chameleon.oo.type.Type;
import chameleon.test.ModelTest;
import chameleon.test.provider.ElementProvider;
import chameleon.util.concurrent.CallableFactory;
import chameleon.util.concurrent.FixedThreadCallableExecutor;
import chameleon.util.concurrent.QueuePollingCallableFactory;
import chameleon.util.concurrent.UnsafeAction;
import chameleon.workspace.Project;
import chameleon.workspace.ProjectException;

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
  
  private static Logger _expressionLogger = Logger.getLogger("chameleon.test.expression");
  
  public static Logger getExpressionLogger() {
  	return _expressionLogger;
  }

  public void setLogLevels() {
  	//Logger.getRootLogger().setLevel(Level.FATAL);
  	getLogger().setLevel(Level.INFO);
		//Logger.getLogger("chameleon.test.expression").setLevel(Level.FATAL);
  }
  
  public int nbThreads() {
	return Runtime.getRuntime().availableProcessors();
  }

/*  
  @Test
  public void testExpressionTypes() throws Exception {
	long startTime = System.nanoTime();
  	Collection<Type> types = typeProvider().elements(language());
  	for(Type type:types) {
  		processType(type);
  	}
	long endTime = System.nanoTime();
	System.out.println("Testing took "+(endTime-startTime)/1000000+" milliseconds.");
  }
*/
  
  @Test
  public void testExpressionTypes() throws Exception {
	  Collection<Type> types = typeProvider().elements(view());
	  final BlockingQueue<Type> typeQueue = new ArrayBlockingQueue<Type>(types.size(), true, types);
	  CallableFactory factory = new QueuePollingCallableFactory(new UnsafeAction<Type,LookupException>() {
	  	public void actuallyPerform(Type type) throws LookupException {
	  		String fullyQualifiedName = type.getFullyQualifiedName();
//	  		getLogger().info("Actually Testing "+fullyQualifiedName);
	  		processType(type);
	  	} 
	  },typeQueue);
	  new FixedThreadCallableExecutor<LookupException>(factory).run();
  }
  
//  @Test
//  public void testExpressionTypes() throws Exception {
//  	long startTime = System.nanoTime();
//  	Collection<Type> types = typeProvider().elements(language());
//  	final Queue<Type> typeQueue = new ConcurrentLinkedQueue(types);
//
//  	UnsafeAction<Type,LookupException> unsafeAction = new UnsafeAction<Type,LookupException>() {
//  		public void actuallyPerform(Type type) throws LookupException {
//  			processType(type);
//  		} 
//  	};
//  	CallableFactory factory = new QueuePollingCallableFactory<Type,LookupException>(unsafeAction,typeQueue);
//  	new FixedThreadCallableExecutor<LookupException>(factory).run();
//  	long endTime = System.nanoTime();
//  	System.out.println("Testing took "+(endTime-startTime)/1000000+" milliseconds.");
//  }
  
  

//  @Test
//  public void testExpressionTypes() throws Exception {
//	  Collection<Type> types = typeProvider().elements(language());
//	  int size = types.size();
//	final BlockingQueue<Type> typeQueue = new ArrayBlockingQueue<Type>(size, true, types);
//	  int nbThreads = nbThreads();
//	ExecutorService executor = Executors.newFixedThreadPool(nbThreads);
//	  getLogger().info("Starting to test "+size + " types.");
////    Iterator<Type> iter = types.iterator();
//    long startTime = System.nanoTime();
//    int count = 1;
//    try {
//      for(int i= 0; i< nbThreads;i++) {
//  	  executor.execute(new Runnable() {
//  		@Override
//  		public void run() {
//  		      try {
//  		    	  boolean ongoing = true;
//  		    	  while(ongoing) {
//  		    		  Type type = typeQueue.poll();
//  		    		  if(type != null) {
//  		    			  getLogger().info("Actually Testing "+type.getFullyQualifiedName());
//  		    			  processType(type);
//  		    		  } else {
//  		    			  ongoing = false;
//  		    		  }
//  		    	  }
//  			} catch (Exception e) {
//  				throw new MyError(e);
//  			}
//  		}
//  	});
//    	
//    }
////    while (iter.hasNext()) {
////      final Type type = iter.next();
////      getLogger().info(count+" Testing "+type.getFullyQualifiedName());
////      count++;
////    }
//    executor.shutdown();
//    executor.awaitTermination(100, TimeUnit.HOURS);
//    } catch(MyError err) {
//    	err.reraise();
//    }
//    long endTime = System.nanoTime();
//    System.out.println("Testing took "+(endTime-startTime)/1000000+" milliseconds.");
//  }
//  
//  private class MyError extends Error {
//	  public Exception exception() {
//		  return _exception;
//	  }
//	  
//	  private Exception _exception;
//	  
//	  public MyError(Exception e) {
//		  _exception = e;
//	  }
//	  
//	  public void reraise() throws Exception {
//		  throw _exception;
//	  }
//  }

  private int _count = 0;
  
   public void processType(Type type) throws LookupException {
    _count++;
    Expression expr = null;
    Object o = null;
//    try {
      List<Expression> exprs = type.descendants(Expression.class);
      for(Expression expression : exprs) {
//      	Syntax syntax = language().plugin(Syntax.class);
//      	if(syntax != null) {
//          getExpressionLogger().info(_count + " Testing: "+syntax.toCode(expression));
//      	} else {
//      		getExpressionLogger().info(_count + " Add a Syntax connector to the language for printing the code of the expression being tested.");
//      	}
        Type expressionType = expression.getType();
				assertTrue(expressionType != null);
//        getExpressionLogger().info(_count + "        : "+expressionType.getFullyQualifiedName());
      }
//    }
//    catch (RuntimeException e) {
//      e.printStackTrace();
//      throw e; 
//    }
  }
  
}
