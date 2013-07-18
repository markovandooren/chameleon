package be.kuleuven.cs.distrinet.chameleon.test;

import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.test.provider.ElementProvider;
import be.kuleuven.cs.distrinet.chameleon.util.AllocationTracker;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputException;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.chameleon.workspace.ProjectException;
import be.kuleuven.cs.distrinet.rejuse.action.Action;

public class CrossReferenceTest extends ModelTest {

	public CrossReferenceTest(Project project,ElementProvider<CrossReference> crossReferenceProvider) throws ProjectException {
		super(project);
		_crossReferenceProvider = crossReferenceProvider;
	}
	
	private ElementProvider<CrossReference> _crossReferenceProvider;

	public ElementProvider<CrossReference> crossReferenceProvider() {
		return _crossReferenceProvider;
	}
	
	@Test
	public void testCrossReferences() throws LookupException, InterruptedException, ExecutionException, InputException {
//  	long startTime = System.nanoTime();
//		Collection<CrossReference> crossReferences = crossReferenceProvider().elements(view());
//		BlockingQueue<CrossReference> queue = new ArrayBlockingQueue<CrossReference>(crossReferences.size(), true, crossReferences);
//		Action<CrossReference,LookupException> action = createAction();
//		CallableFactory factory = new QueuePollingCallableFactory(action,queue);
//		new FixedThreadCallableExecutor<LookupException>(factory).run();
		
//		TRACKER.clearAllocationMap();
		
  	project().applyToSource(createAction());
//  	long endTime = System.nanoTime();
//  	System.out.println("Testing took "+(endTime-startTime)/1000000+" milliseconds.");
//  	System.out.println("Number of avoidable getOtherEnds() computations: "+Association.nbAvoidableGetOtherEnds());
//  	System.out.println("Number of getOtherEnds() computations that are done only once: "+Association.nbWithoutAvoidableGetOtherEnds());

//  	Map<Class,Integer> perClass  = TRACKER.nbAvoidableAllocationsPerClass();
//  	List<Map.Entry<Class,Integer>> list = new ArrayList<>(perClass.entrySet());
//  	Collections.sort(list, new EntryComparator());
//  	for(Map.Entry<Class,Integer> entry: list) {
//  		System.out.println("Avoidable in class: "+ entry.getKey().getName() +" : "+entry.getValue());
//  	}
 
//  	Map<String,Integer> perAssociation = nbAvoidableGetOtherEndsPerAssociation();
//  	List<Map.Entry<String,Integer>> alist = new ArrayList<>(perAssociation.entrySet());
//  	Collections.sort(alist, new EntryComparator());
//  	for(Map.Entry<String,Integer> entry: alist) {
//  		System.out.println("Avoidable in assocation: "+ entry.getKey() +" : "+entry.getValue());
//  	}
  	
//  	Association.cleanGetOtherEndsCache();
	}

	protected Action<CrossReference, LookupException> createAction() {
		return new Action<CrossReference,LookupException>(CrossReference.class) {
			public void perform(CrossReference cref) throws LookupException {
				Declaration declaration = cref.getElement();
				assertTrue(declaration != null);
			} 
		};
	}
	
	private static class EntryComparator implements Comparator<Map.Entry<?,Integer>> {

		@Override
		public int compare(Map.Entry<?,Integer> o1, Map.Entry<?,Integer> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
		
	}
	
//	public final static AllocationTracker TRACKER = new AllocationTracker();
	
//	public static Map<String,Integer> nbAvoidableGetOtherEndsPerAssociation() {
//		Map<String,Integer> result = new HashMap<String, Integer>();
//		for(Map.Entry<Association, Integer> entry: Association._nbTimesGetOtherEnds.entrySet()) {
//			Association key = entry.getKey();
//			if(key instanceof ChameleonAssociation) {
//				ChameleonAssociation chameleonAssociation = (ChameleonAssociation)key;
//				String role = chameleonAssociation.getObject().getClass().getName()+"."+chameleonAssociation.role();
//				Integer count = entry.getValue();
//				Integer accumulated = result.get(role);
//				if(accumulated == null) {
//					accumulated = count - 1;
//				} else {
//					accumulated = accumulated + count - 1;
//				}
//				result.put(role, accumulated);
//			}
//		}
//		return result;
//	}

}
