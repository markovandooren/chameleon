package chameleon.test;

import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Test;

import chameleon.core.declaration.Declaration;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;
import chameleon.input.ParseException;
import chameleon.test.provider.ElementProvider;
import chameleon.test.provider.ModelProvider;
import chameleon.util.concurrent.CallableFactory;
import chameleon.util.concurrent.FixedThreadCallableExecutor;
import chameleon.util.concurrent.QueuePollingCallableFactory;
import chameleon.util.concurrent.UnsafeAction;

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
	public void testCrossReferences() throws LookupException, InterruptedException {
		Collection<CrossReference> crossReferences = crossReferenceProvider().elements(language());
		final BlockingQueue<CrossReference> queue = new ArrayBlockingQueue<CrossReference>(crossReferences.size(), true, crossReferences);
		CallableFactory factory = new QueuePollingCallableFactory(new UnsafeAction<CrossReference,LookupException>() {
			public void actuallyPerform(CrossReference cref) throws LookupException {
				Declaration declaration;
			    declaration = cref.getElement();
				assertTrue(declaration != null);
			} 

		},queue);
		new FixedThreadCallableExecutor<LookupException>(factory).run();
	}
//	@Test
//	public void testCrossReferences() throws LookupException, InterruptedException {
//		Collection<CrossReference> crossReferences = crossReferenceProvider().elements(language());
//		final BlockingQueue<CrossReference> queue = new ArrayBlockingQueue<CrossReference>(crossReferences.size(), true, crossReferences);
//		RunnableFactory factory = new QueuePollingRunnableFactory(new SafeAction<CrossReference>() {
//			public void actuallyPerform(CrossReference cref) throws LookupException {
//				Declaration declaration;
//			    declaration = cref.getElement();
//				assertTrue(declaration != null);
//			} 
//
//		},queue);
//		new UnsafeFixedThreadExecutor<LookupException>(factory,LookupException.class).run();
//	}
}
