package org.aikodi.chameleon.analysis.dependency;

import org.aikodi.chameleon.analysis.Analyzer;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.workspace.InputException;
import org.aikodi.chameleon.workspace.Project;

import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.function.Function;
import be.kuleuven.cs.distrinet.rejuse.graph.Edge;
import be.kuleuven.cs.distrinet.rejuse.graph.UniEdge;
import be.kuleuven.cs.distrinet.rejuse.predicate.True;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public abstract class DependencyAnalyzer<D extends Declaration> extends Analyzer {

	protected abstract UniversalPredicate<D,Nothing> elementPredicate();

	protected UniversalPredicate<? super CrossReference<?>,Nothing> crossReferencePredicate() {
		return new True();
	}

	protected Function<D, D,Nothing> createMapper() {
		return new Function<D, D,Nothing>() {
			@Override
			public D apply(D declaration) {
				return declaration;
			}
		};
	}
	
	protected abstract UniversalPredicate<D,Nothing> declarationPredicate();

	public DependencyAnalyzer(Project project) {
		super(project);
	}
	
	public static interface GraphBuilder<V> {
		
		public void addVertex(V v);
		
		public void addEdge(V first, V second);
	}
	
	public void buildGraph(final GraphBuilder<Element> builder) throws InputException {
		Function<D,D,Nothing> function = createMapper();
		UniversalPredicate<D, Nothing> elementPredicate = elementPredicate();
		DependencyAnalysis<D, D> analysis = new DependencyAnalysis<D,D>(
				elementPredicate, 
				crossReferencePredicate(),
				function,
				declarationPredicate(), new True(),
				new DependencyAnalysis.NOOP());
		
		DependencyResult result = analysisResult(analysis);
		Action<Element, Nothing> nodeAction = new Action<Element, Nothing>(Element.class) {
			@Override
			protected void doPerform(Element d) {
				builder.addVertex(d);
			}
		};
		Action<Edge, Nothing> edgeAction = new Action<Edge, Nothing>(Edge.class) {
			@Override
			protected void doPerform(Edge d) {
				UniEdge<D> edge = (UniEdge<D>) d;
				builder.addEdge(edge.start(), edge.end());
			}
		};
		result.traverse(nodeAction, edgeAction);
		
	}

}