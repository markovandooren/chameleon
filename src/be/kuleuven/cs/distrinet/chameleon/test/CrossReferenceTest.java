package be.kuleuven.cs.distrinet.chameleon.test;

import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.test.provider.ElementProvider;
import be.kuleuven.cs.distrinet.chameleon.util.concurrent.CallableFactory;
import be.kuleuven.cs.distrinet.chameleon.util.concurrent.FixedThreadCallableExecutor;
import be.kuleuven.cs.distrinet.chameleon.util.concurrent.QueuePollingCallableFactory;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.chameleon.workspace.ProjectException;
import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.association.Association;

public class CrossReferenceTest extends ModelTest {

	public CrossReferenceTest(Project provider,ElementProvider<CrossReference> crossReferenceProvider) throws ProjectException {
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
		Collection<CrossReference> crossReferences = crossReferenceProvider().elements(view());
		final BlockingQueue<CrossReference> queue = new ArrayBlockingQueue<CrossReference>(crossReferences.size(), true, crossReferences);
		CallableFactory factory = new QueuePollingCallableFactory(new Action<CrossReference,LookupException>(CrossReference.class) {
			public void perform(CrossReference cref) throws LookupException {
				Declaration declaration;
			    declaration = cref.getElement();
				assertTrue(declaration != null);
			} 

		},queue);
		new FixedThreadCallableExecutor<LookupException>(factory).run();
  	long endTime = System.nanoTime();
  	System.out.println("Testing took "+(endTime-startTime)/1000000+" milliseconds.");
  	System.out.println("Number of avoidable getOtherEnds() computations: "+Association.nbAvoidableGetOtherEnds());
  	System.out.println("Number of getOtherEnds() computations that are done only once: "+Association.nbWithoutAvoidableGetOtherEnds());
  	Map<Class,Integer> perClass  = Association.nbAvoidableGetOtherEndsPerClass();
  	List<Map.Entry<Class,Integer>> list = new ArrayList<>(perClass.entrySet());
  	Collections.sort(list, new EntryComparator());
  	for(Map.Entry<Class,Integer> entry: list) {
  		System.out.println("Avoidable in class: "+ entry.getKey().getName() +" : "+entry.getValue());
  	}
  	Association.cleanGetOtherEndsCache();
	}
	
	private static class EntryComparator implements Comparator<Map.Entry<Class,Integer>> {

		@Override
		public int compare(Map.Entry<Class,Integer> o1, Map.Entry<Class,Integer> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
		
	}
}
