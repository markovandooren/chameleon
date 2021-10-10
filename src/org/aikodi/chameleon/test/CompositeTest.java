package org.aikodi.chameleon.test;

import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.test.provider.BasicDescendantProvider;
import org.aikodi.chameleon.test.provider.BasicNamespaceProvider;
import org.aikodi.chameleon.workspace.ConfigException;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.chameleon.workspace.ProjectException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class CompositeTest {

	public final static String LoggerName = "org.aikodi.chameleon.test";
	public final static Logger Logger = LogManager.getLogger(LoggerName);
	
	protected abstract ExecutorService threadPool();
	
//	/**
//	 * Test clone for all elements in the namespaces provided
//	 * by the namespace provider.
//	 */
//	@Test
//	public void testClone() throws Exception {
//		new CloneAndChildTest(project(), namespaceProvider(),threadPool()).testClone();
//	}

	// NO LONGER REQUIRED NOW children() is implemented with reflection (making the test identical to the implementation)
//	/**
//	 * Test children for all elements in the namespaces provided
//	 * by the namespace provider.
//	 */
//	@Test
//	public void testChildren() throws Exception {
//		ChildrenTest childrenTest = new ChildrenTest(project(), namespaceProvider());
//		childrenTest.excludeFieldName(ElementImpl.class, "_parentLink");
//		childrenTest.excludeFieldName(NamespaceDeclaration.class, "_namespaceLink");
//		addExcludes(childrenTest);
//		childrenTest.testChildren();
//	}
	
	/**
	 * Exclude Association fields that should not be children. This method is invoked by
	 * testChildren(), which already excludes ElementImpl._parentLink and NamespacePart._namespaceLink. 
	 */
	public void addExcludes(ChildrenTest test) {
		
	}

	/**
	 * Test getElement and getDeclarator for all cross-reference in all namespaces
	 * provided by the namespace provider.
	 */
	@Test
	public void testCrossReferences() throws Exception {
		new CrossReferenceTest(project(), namespaceProvider(), Executors.newCachedThreadPool()).testCrossReferences();
//		new CrossReferenceTest(project(), new BasicDescendantProvider<CrossReference>(namespaceProvider(), CrossReference.class), Executors.newCachedThreadPool()).testCrossReferences();
	}

	/**
	 * Test the verification by invoking verify() for all namespace parts, and checking if the result is valid.
	 */
	@Test
	public void testVerification() throws Exception {
		new VerificationTest(project(), new BasicDescendantProvider<NamespaceDeclaration>(namespaceProvider(), NamespaceDeclaration.class)).testVerification();
	}

	/**
	 * A provider for the model to be tested.
	 * @throws ProjectException 
	 * @throws ConfigException 
	 */
	protected abstract Project makeProject() throws ConfigException;

	public Project project() throws ConfigException {
		return makeProject();
	}
	/**
	 * A provider for the namespaces to be tested.
	 */
	public abstract BasicNamespaceProvider namespaceProvider();

  /**
	 * Return the symbol for separating directories from each other
	 */
 /*@
   @ public behavior
   @
   @ post \result == File.separator;
   @*/
	public String separator() {
	  return File.separator;
	}


}
