package chameleon.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chameleon.core.lookup.LookupException;
import chameleon.core.namespacedeclaration.NamespaceDeclaration;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.test.provider.ElementProvider;
import chameleon.workspace.Project;
import chameleon.workspace.ProjectException;

public class VerificationTest extends ModelTest {

	public VerificationTest(Project provider,ElementProvider<NamespaceDeclaration> compilationUnitProvider) throws ProjectException {
		super(provider);
		_elementProvider = compilationUnitProvider;
	}

	private ElementProvider<NamespaceDeclaration> _elementProvider;

	public ElementProvider<NamespaceDeclaration> elementProvider() {
		return _elementProvider;
	}
	
	@Test
	public void testVerification() throws LookupException {
		for(NamespaceDeclaration element: elementProvider().elements(project())) {
			VerificationResult result = element.verify();
			assertTrue(result.toString() ,Valid.create() == result);
		}
	}

}
