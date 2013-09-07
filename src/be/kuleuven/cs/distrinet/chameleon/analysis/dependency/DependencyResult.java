package be.kuleuven.cs.distrinet.chameleon.analysis.dependency;

import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.analysis.Result;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.graph.Edge;
import be.kuleuven.cs.distrinet.rejuse.graph.Graph;
import be.kuleuven.cs.distrinet.rejuse.graph.UniEdge;
import be.kuleuven.cs.distrinet.rejuse.graph.UniEdgeFactory;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;

public class DependencyResult extends Result<DependencyResult> {

	public DependencyResult() {
		_dependencyGraph = new Graph<Element>(new UniEdgeFactory<Element>());
	}
	
	private DependencyResult(Graph<Element> dependencies) {
		_dependencyGraph = dependencies;
	}
	
	private Graph<Element> _dependencyGraph;

	private Graph<Element> dependencyGraph() {
		return _dependencyGraph;
	}
	
	public Set<Element> dependencies(Element source) {
		return _dependencyGraph.node(source).directSuccessors();
	}
	
	public Set<UniEdge<Element>> dependencies() {
		// Can't type this properly. Maybe it can be done if we put all type parameters
		// in the factories and not make them mutually recursive.
		return (Set) dependencyGraph().edges();
	}
	
	public <X extends Exception> void traverse(Action<? super Element, ? extends X> nodeAction, Action<? super Edge<Element>,? extends X> edgeAction) throws X {
		_dependencyGraph.traverseAll(nodeAction, edgeAction);
	}
	
	@Override
	public String message() {
		return "TODO";
	}

	@Override
	public DependencyResult and(DependencyResult other) {
		return new DependencyResult(_dependencyGraph.plus(other._dependencyGraph));
	}
	
	public <E extends Exception> void filter(Predicate<Edge<Element>,E> predicate) throws E {
		_dependencyGraph.filter(predicate);
	}
	
	void add(Element element, Declaration declaration) {
		dependencyGraph().addNode(element);
		dependencyGraph().addNode(declaration);
		dependencyGraph().ensureEdge(element, declaration);
	}
	
	void add(Dependency<?,?,Declaration> dependency) {
		add(dependency.source(), dependency.target());
	}
	
	public void prune() {
		_dependencyGraph.prune();
	}
}
