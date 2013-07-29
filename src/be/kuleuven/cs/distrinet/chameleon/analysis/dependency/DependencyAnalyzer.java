package be.kuleuven.cs.distrinet.chameleon.analysis.dependency;

import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.analysis.Analyzer;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.util.Pair;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputException;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;
import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;

import com.google.common.base.Function;

public abstract class DependencyAnalyzer<D extends Declaration> extends Analyzer {

	protected Predicate<Pair<D,D>> _originPredicate;

	protected abstract Function<D, D> createMapper();

	protected Predicate<CrossReference<?>> _crossReferencePredicate;

	public DependencyAnalyzer(Project project, Class<D> type) {
		super(project);
		_class = type;
	}
	
	private Class<D> _class;

//	protected void filter(DependencyResult result) {
//		Map<D,Set<D>> deps = result.dependencies();
//		for(final Map.Entry<D, Set<D>> first : deps.entrySet()) {
//			// if a type depends on S and T and S <: T then remove T to make the graph cleaner
//			// might give a deceptive view though.
//			new SafePredicate<D>() {
//				@Override
//				public boolean eval(final D o1) {
//					return new SafePredicate<D>() {
//	
//						@Override
//						public boolean eval(D o2) {
//							try {
//								return o1 == o2 || (! o2.subTypeOf(o1));
//							} catch (LookupException e) {
//								// don't filter if a lookup exception is thrown because we don't know
//								// if it is safe to filter.
//								e.printStackTrace();
//								return true;
//							} 
//						}
//					}.forAll(first.getValue());
//				}
//			}.filter(first.getValue());
//			
//			
//			
//			for(Map.Entry<D, Set<D>> second : deps.entrySet()) {
//				try {
//					D t1 = first.getKey();
//					D t2 = second.getKey();
//					if(t1 != t2 && t1.subTypeOf(t2)) {
//						first.getValue().removeAll(second.getValue());
//					}
//				} catch (LookupException e) {
//					// don't filter if a lookup exception is thrown because we don't know
//					// if it is safe to filter.
//					e.printStackTrace();
//				}
//			}
//		}
//	}

	public static interface GraphBuilder<V> {
		
		public void addVertex(V v);
		
		public void addEdge(V first, V second);
	}
	
	public void buildGraph(GraphBuilder<D> builder) throws InputException {
		Function<D,D> function = createMapper();
		DependencyResult result = analysisResult(new DependencyAnalysis<D,D>(_class, _class, _originPredicate, _crossReferencePredicate, function));
//		filter(result);
		Map<D,Set<D>> deps = result.dependencies();
		for(Map.Entry<D,Set<D>> dependencies: deps.entrySet()) {
			D origin = dependencies.getKey();
			builder.addVertex(origin);
			for(D dependency: dependencies.getValue()) {
				builder.addVertex(dependency);
				builder.addEdge(origin, dependency);
			}
		}
	}

}