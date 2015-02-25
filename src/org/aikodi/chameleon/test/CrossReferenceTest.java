package org.aikodi.chameleon.test;

import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.test.provider.ElementProvider;
import org.aikodi.chameleon.util.concurrent.CallableFactory;
import org.aikodi.chameleon.util.concurrent.FixedThreadCallableExecutor;
import org.aikodi.chameleon.util.concurrent.QueuePollingCallableFactory;
import org.aikodi.chameleon.workspace.InputException;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.chameleon.workspace.ProjectException;
import org.junit.Test;

import be.kuleuven.cs.distrinet.rejuse.action.Action;

public class CrossReferenceTest extends ModelTest {

	public CrossReferenceTest(Project project,ElementProvider<Namespace> crossReferenceProvider,ExecutorService executor) throws ProjectException {
		super(project);
		_crossReferenceProvider = crossReferenceProvider;
		threadPool = executor;
	}
	
	private ElementProvider<Namespace> _crossReferenceProvider;

	public ElementProvider<Namespace> crossReferenceProvider() {
		return _crossReferenceProvider;
	}
  ExecutorService threadPool;

	@Test
	public void testCrossReferences() throws LookupException, InterruptedException, ExecutionException, InputException {
//  	long startTime = System.nanoTime();
		Collection<Namespace> crossReferences = crossReferenceProvider().elements(view());
	  Collection<Namespace> namespaces = new ArrayList<>();
	  for(Namespace ns: crossReferences) {
	  	namespaces.addAll(ns.getAllSubNamespaces());
	  }
		BlockingQueue<Namespace> queue = new ArrayBlockingQueue<Namespace>(namespaces.size(), true, namespaces);
		Action<Namespace,LookupException> action = createAction();
		CallableFactory factory = new QueuePollingCallableFactory<Namespace,LookupException>(action,queue);
		new FixedThreadCallableExecutor<LookupException>(factory,threadPool).run();
		
//		TRACKER.clearAllocationMap();
		
//  	project().applyToSource(createAction());
  	
//  	for(CrossReference type: crossReferenceProvider().elements(view())) {
//  		type.apply(createAction());
//  	}

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

	protected Action<Namespace, LookupException> createAction() {
		return new Action<Namespace,LookupException>(Namespace.class) {
			@Override
         public void doPerform(Namespace ns) throws LookupException {
				for(NamespaceDeclaration nsp: ns.getNamespaceParts()) {
					for(CrossReference cref: nsp.descendants(CrossReference.class)) {
						Declaration declaration = cref.getElement();
						assertTrue(declaration != null);
					}
				} 
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
