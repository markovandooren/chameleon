package org.aikodi.chameleon.analysis.dependency;

import java.util.Set;
import java.util.stream.Collectors;

import org.aikodi.chameleon.analysis.Result;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;

import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.collection.CollectionOperations;
import be.kuleuven.cs.distrinet.rejuse.graph.Edge;
import be.kuleuven.cs.distrinet.rejuse.graph.Graph;
import be.kuleuven.cs.distrinet.rejuse.graph.Node;
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
	
    public Set<Node<Element>> dependencyNodes(Element source) {
      return _dependencyGraph.node(source).directSuccessorNodes();
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
		Graph<Element> dependencyGraph = dependencyGraph();
		dependencyGraph.addNode(element);
		dependencyGraph().addNode(declaration);
		Edge<Element> edge = dependencyGraph().ensureEdge(element, declaration);
		DependencyCount counter = edge.get(DependencyCount.class);
		if(counter == null) {
			counter = new DependencyCount();
			edge.put(DependencyCount.class,counter);
		} else {
			counter.increase();
		}
	}
	
	public boolean involvedInCyle(Element element) {
		Node<Element> node = _dependencyGraph.node(element);
		return node == null ? false : node.canReach(node);
	}
	
	public static class DependencyCount {
		public void increase() {
			_count++;
		}
		
		public int value() {
			return _count;
		}
		
		private int _count=1;
	}
	
	void add(Dependency<?,?,Declaration> dependency) {
		add(dependency.source(), dependency.target());
	}
	
	public void prune() {
		_dependencyGraph.remoteEdgesNotInvolvedInCycles();
	}
	
	public Set<Node<Element>> nodes() {
	  return _dependencyGraph.nodes();
	}
	
	public Node<Element> nodeOf(Element element) {
	  return _dependencyGraph.node(element);
	}
	
	public Set<Element> elements() {
	  return nodes().stream().map(n -> n.object()).collect(Collectors.toSet());
	}
}
