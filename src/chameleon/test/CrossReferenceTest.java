package chameleon.test;

import java.io.IOException;

import org.junit.Test;

import chameleon.core.declaration.Declaration;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;
import chameleon.input.ParseException;
import chameleon.test.provider.ElementProvider;
import chameleon.test.provider.ModelProvider;

public class CrossReferenceTest extends ModelTest {

	public CrossReferenceTest(ModelProvider provider,ElementProvider<CrossReference> crossReferenceProvider) throws ParseException, IOException {
		super(provider);
		_crossReferenceProvider = crossReferenceProvider;
	}
	
	private ElementProvider<CrossReference> _crossReferenceProvider;

	public ElementProvider<CrossReference> crossReferenceProvider() {
		return _crossReferenceProvider;
	}
	
	@Test
	public void testCrossReferences() throws LookupException {
		for(CrossReference crossReference: crossReferenceProvider().elements(language())) {
			Declaration declaration = crossReference.getElement();
			Declaration declarator = crossReference.getDeclarator();
		}
	}
}
