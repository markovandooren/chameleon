package be.kuleuven.cs.distrinet.chameleon.test;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.distrinet.chameleon.core.Config;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.test.provider.BasicDescendantProvider;
import be.kuleuven.cs.distrinet.chameleon.test.provider.BasicNamespaceProvider;
import be.kuleuven.cs.distrinet.chameleon.workspace.ConfigException;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.chameleon.workspace.ProjectException;

public abstract class CompositeTest {

	@Before
	public void setCaching() {
	  Config.setCaching(true);
	}

	/**
	 * Test clone for all elements in the namespaces provided
	 * by the namespace provider.
	 */
	@Test
	public void testClone() throws Exception {
		new CloneAndChildTest(project(), namespaceProvider()).testClone();
	}

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
		new CrossReferenceTest(project(), new BasicDescendantProvider<CrossReference>(namespaceProvider(), CrossReference.class)).testCrossReferences();
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
