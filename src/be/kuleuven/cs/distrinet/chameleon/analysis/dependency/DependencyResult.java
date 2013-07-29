package be.kuleuven.cs.distrinet.chameleon.analysis.dependency;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.analysis.Result;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;

public class DependencyResult<E extends Element,D extends Declaration> extends Result<DependencyResult<E,D>> {

	public DependencyResult(E element, Set<D> dependencies) {
		Set<D> myDeps = new HashSet<D>(dependencies);
		_dependencies = new HashMap<E,Set<D>>();
		_dependencies.put(element, myDeps);
	}
	
	public DependencyResult() {
		_dependencies = new HashMap<E,Set<D>>();
	}
	
	private DependencyResult(DependencyResult<E,D> first, DependencyResult<E,D> second) {
		_dependencies = new HashMap<E,Set<D>>();
		_dependencies.putAll(first._dependencies);
		_dependencies.putAll(second._dependencies);
	}
	
	public Set<D> dependencies(E element) {
		return new HashSet<D>(_dependencies.get(element));
	}
	
	@Override
	public String message() {
		return "TODO";
	}

	@Override
	public DependencyResult<E,D> and(DependencyResult<E,D> other) {
		if(other == null) {
			return this;
		} else {
			return new DependencyResult<E,D>(this, other);
		}
	}

	public Map<E,Set<D>> dependencies() {
		return new HashMap<E,Set<D>>(_dependencies);
	}

	private Map<E, Set<D>> _dependencies;
}
