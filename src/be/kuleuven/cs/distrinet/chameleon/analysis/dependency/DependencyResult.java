package be.kuleuven.cs.distrinet.chameleon.analysis.dependency;

import be.kuleuven.cs.distrinet.chameleon.analysis.Result;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.rejuse.graph.Graph;
import be.kuleuven.cs.distrinet.rejuse.graph.UniEdgeFactory;

public class DependencyResult extends Result<DependencyResult> {

	public DependencyResult() {
		_dependencies = new Graph<Element>(new UniEdgeFactory<Element>());
	}
	
	private DependencyResult(Graph<Element> dependencies) {
		_dependencies = dependencies;
	}
	
	private Graph<Element> _dependencies;

	protected Graph<Element> dependencies() {
		return _dependencies;
	}
	
	@Override
	public String message() {
		return "TODO";
	}

	@Override
	public DependencyResult and(DependencyResult other) {
		return new DependencyResult(_dependencies.plus(other._dependencies));
	}
	
	public void add(E element, D declaration) {
		dependencies().addNode(element);
		dependencies().addNode(declaration);
		dependencies().ensureEdge(element, declaration);
	}
}
