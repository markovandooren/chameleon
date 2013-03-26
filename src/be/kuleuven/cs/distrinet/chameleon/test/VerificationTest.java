package be.kuleuven.cs.distrinet.chameleon.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.test.provider.ElementProvider;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.chameleon.workspace.ProjectException;

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
		for(NamespaceDeclaration element: elementProvider().elements(view())) {
			VerificationResult result = element.verify();
			assertTrue(result.toString() ,Valid.create() == result);
		}
	}

}
