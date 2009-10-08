package chameleon.test;

import org.junit.Before;
import org.junit.Test;

import chameleon.core.Config;
import chameleon.core.namespacepart.NamespacePart;
import chameleon.core.reference.CrossReference;
import chameleon.test.provider.BasicDescendantProvider;
import chameleon.test.provider.BasicNamespaceProvider;
import chameleon.test.provider.ModelProvider;

public abstract class CompositeTest {

	@Before
	public void setCaching() {
	  Config.setCaching(true);
	}

	/**
	 * Test clone and children for all elements in the namespaces provided
	 * by the namespace provider.
	 */
	@Test
	public void testCloneAndChildren() throws Exception {
		new CloneAndChildTest(modelProvider(), namespaceProvider()).testClone();
	}

	/**
	 * Test getElement and getDeclarator for all cross-reference in all namespaces
	 * provided by the namespace provider.
	 */
	@Test
	public void testCrossReferences() throws Exception {
		new CrossReferenceTest(modelProvider(), new BasicDescendantProvider<CrossReference>(namespaceProvider(), CrossReference.class)).testCrossReferences();
	}

	/**
	 * Test the verification by invoking verify() for all namespace parts, and checking if the result is valid.
	 */
	@Test
	public void testVerification() throws Exception {
		new VerificationTest(modelProvider(), new BasicDescendantProvider<NamespacePart>(namespaceProvider(), NamespacePart.class)).testVerification();
	}

	/**
	 * A provider for the model to be tested.
	 */
	public abstract ModelProvider modelProvider();

	/**
	 * A provider for the namespaces to be tested.
	 */
	public abstract BasicNamespaceProvider namespaceProvider();

}
