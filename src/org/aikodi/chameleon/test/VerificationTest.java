package org.aikodi.chameleon.test;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.test.provider.ElementProvider;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.chameleon.workspace.ProjectException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

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
			Verification result = element.verify();
			assertTrue(result.toString() ,Valid.create() == result);
		}
	}

}
