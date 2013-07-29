package be.kuleuven.cs.distrinet.chameleon.analysis.dependency;

import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.analysis.Result;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.rejuse.graph.Graph;
import be.kuleuven.cs.distrinet.rejuse.graph.UniEdgeFactory;

public class DependencyResult<E extends Element,D extends Declaration> extends Result<DependencyResult<E,D>> {

	public DependencyResult() {
		_dependencies = new Graph<Element>(new UniEdgeFactory<Element>());
	}
	
	private Graph<Element> _dependencies;

	@Override
	public String message() {
		return "TODO";
	}

	@Override
	public DependencyResult<E, D> and(DependencyResult<E, D> other) {
		todo
	}
}
