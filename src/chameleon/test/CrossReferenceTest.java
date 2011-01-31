package chameleon.test;

import static org.junit.Assert.assertTrue;

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
			System.out.println(crossReference.getClass());
			
			Declaration declaration = crossReference.getElement();
			assertTrue(declaration != null);
			// The declarator test isn't necessary since every implementation simply
			// creates a DeclaratorSelector using its own selector, and then performs a lookup
			// since the lookup path is exactly the same (it uses the original selector), testing 
			// this for a single element is sufficient.
			//Declaration declarator = crossReference.getDeclarator();
		}
	}
}
