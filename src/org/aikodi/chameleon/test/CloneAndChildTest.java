package org.aikodi.chameleon.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.input.ParseException;
import org.aikodi.chameleon.test.provider.ElementProvider;
import org.aikodi.chameleon.util.concurrent.CallableFactory;
import org.aikodi.chameleon.util.concurrent.FixedThreadCallableExecutor;
import org.aikodi.chameleon.util.concurrent.QueuePollingCallableFactory;
import org.aikodi.chameleon.workspace.InputException;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.chameleon.workspace.ProjectException;
import org.aikodi.rejuse.action.Action;
import org.junit.Test;

/**
 * A test class for the clone and children methods of elements. It test all elements
 * in the namespaces of its namespace provider.
 * 
 * @author Marko van Dooren
 */
public class CloneAndChildTest extends ModelTest {



	/**
	 * Create a new clone a child tester with the given model provider and namespace provider.
	 * @throws IOException 
	 * @throws ParseException 
	 */
 /*@
   @ public behavior
   @
   @ pre provider != null;
   @ pre namespaceProvider != null;
   @
   @ post modelProvider() == provider;
   @ post namespaceProvider() == namespaceProvider;
   @*/
	public CloneAndChildTest(Project project, ElementProvider<Namespace> namespaceProvider, ExecutorService executor) throws ProjectException {
		super(project);
		_namespaceProvider = namespaceProvider;
		threadPool = executor;
	}
	
	private ExecutorService threadPool;
	
	private ElementProvider<Namespace> _namespaceProvider;

	public ElementProvider<Namespace> namespaceProvider() {
		return _namespaceProvider;
	}
	
	@Test
	public void testClone() throws LookupException, InputException, InterruptedException, ExecutionException {
//		project().applyToSource(new Action<Element, Nothing>(Element.class) {
//			@Override
//			public void doPerform(Element object) throws Nothing {
//				test(object);
//			}
//		});
	  Collection<Namespace> types = namespaceProvider().elements(view());
	  Collection<Namespace> namespaces = new ArrayList<>();
	  for(Namespace ns: types) {
	  	namespaces.addAll(ns.descendantNamespaces());
	  }
	  final BlockingQueue<Namespace> typeQueue = new ArrayBlockingQueue<Namespace>(namespaces.size(), true, namespaces);
	  Action<Namespace,LookupException> action = createAction();
		CallableFactory factory = new QueuePollingCallableFactory(action,typeQueue);
	  new FixedThreadCallableExecutor<LookupException>(factory,threadPool).run();

	}
	
	protected Action<Namespace, LookupException> createAction() {
		return new Action<Namespace,LookupException>(Namespace.class) {
	  	@Override
      public void doPerform(Namespace type) throws LookupException {
	  		List<NamespaceDeclaration> namespaceParts = type.namespaceDeclarations();
				for(NamespaceDeclaration nsp: namespaceParts) {
	  			for(Element element: nsp.lexical().descendants()) {
	  				test(type);
	  			}
	  		}
	  	} 
	  };
	}


	/**
	 * Test the clone method of the given element.
	 * 
	 * The test fails if the clone method modifies the given element.
   * The test fails if the element has null as its one of its children.
   * The test fails if the clone does not have the same amount of children as
   * the given element.
   * The test fails if the element is derived. Derived element should never be reachable
   * from the model through the lexical navigation methods of Element.
   * The test fails if the clone is null.
   * The test fails if the clone has null as one of its children.  
	 */
	private void test(Element element) {
		String msg = "element type:"+element.getClass().getName();
		assertFalse(element.isDerived());
		List<Element> children = (List<Element>) element.children();
		assertNotNull(msg,children);
		// Testing for null in the children is already done by the children test.
		//assertFalse(msg,children.contains(null));
		Element clone = element.clone();
		clone.setUniParent(element);
		assertNotNull(msg,clone);
		List<Element> clonedChildren = (List<Element>) clone.children();
		List<Element> newChildren = (List<Element>) element.children();
		assertNotNull(msg,clonedChildren);
		assertFalse(msg,clonedChildren.contains(null));
		assertEquals(msg,children.size(), newChildren.size());
		try {
		  assertEquals(msg,children, newChildren);
		} catch(AssertionError err) {
			children.get(1).equals(newChildren.get(1));
			throw err;
		}
		assertEquals(msg,children.size(), clonedChildren.size());
	}
	
}
