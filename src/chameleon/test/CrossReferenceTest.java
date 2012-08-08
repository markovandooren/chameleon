package chameleon.test;

import static junit.framework.Assert.assertTrue;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import chameleon.core.declaration.Declaration;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;
import chameleon.test.provider.ElementProvider;
import chameleon.util.concurrent.CallableFactory;
import chameleon.util.concurrent.FixedThreadCallableExecutor;
import chameleon.util.concurrent.QueuePollingCallableFactory;
import chameleon.util.concurrent.UnsafeAction;
import chameleon.workspace.ProjectException;
import chameleon.workspace.ProjectBuilder;

public class CrossReferenceTest extends ModelTest {

	public CrossReferenceTest(ProjectBuilder provider,ElementProvider<CrossReference> crossReferenceProvider) throws ProjectException {
		super(provider);
		_crossReferenceProvider = crossReferenceProvider;
	}
	
	private ElementProvider<CrossReference> _crossReferenceProvider;

	public ElementProvider<CrossReference> crossReferenceProvider() {
		return _crossReferenceProvider;
	}
	
	@Test
	public void testCrossReferences() throws LookupException, InterruptedException, ExecutionException {
  	long startTime = System.nanoTime();
		Collection<CrossReference> crossReferences = crossReferenceProvider().elements(project());
		final BlockingQueue<CrossReference> queue = new ArrayBlockingQueue<CrossReference>(crossReferences.size(), true, crossReferences);
		CallableFactory factory = new QueuePollingCallableFactory(new UnsafeAction<CrossReference,LookupException>() {
			public void actuallyPerform(CrossReference cref) throws LookupException {
				Declaration declaration;
			    declaration = cref.getElement();
				assertTrue(declaration != null);
			} 

		},queue);
		new FixedThreadCallableExecutor<LookupException>(factory).run();
  	long endTime = System.nanoTime();
  	System.out.println("Testing took "+(endTime-startTime)/1000000+" milliseconds.");
	}
}
