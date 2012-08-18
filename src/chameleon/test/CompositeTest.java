package chameleon.test;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import chameleon.core.Config;
import chameleon.core.element.ElementImpl;
import chameleon.core.namespacedeclaration.NamespaceDeclaration;
import chameleon.core.reference.CrossReference;
import chameleon.test.provider.BasicDescendantProvider;
import chameleon.test.provider.BasicNamespaceProvider;
import chameleon.workspace.Project;
import chameleon.workspace.ProjectBuilder;
import chameleon.workspace.ProjectException;

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

	/**
	 * Test children for all elements in the namespaces provided
	 * by the namespace provider.
	 */
	@Test
	public void testChildren() throws Exception {
		ChildrenTest childrenTest = new ChildrenTest(project(), namespaceProvider());
		childrenTest.excludeFieldName(ElementImpl.class, "_parentLink");
		childrenTest.excludeFieldName(NamespaceDeclaration.class, "_namespaceLink");
		addExcludes(childrenTest);
		childrenTest.testChildren();
	}
	
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
	 */
	protected abstract Project makeProject() throws ProjectException;

	public Project project() throws ProjectException {
		long start = System.nanoTime();
		Project result = makeProject();
		long stop = System.nanoTime();
		System.out.println("Model input took "+(stop-start)/1000000+" milliseconds.");
		return result;
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
