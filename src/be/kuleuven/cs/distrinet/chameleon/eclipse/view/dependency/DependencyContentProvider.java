package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import org.eclipse.gef4.zest.core.viewers.IGraphContentProvider;
import org.eclipse.jface.viewers.Viewer;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
import be.kuleuven.cs.distrinet.rejuse.graph.UniEdge;

public class DependencyContentProvider implements IGraphContentProvider {

	@Override
	public void dispose() {
		_result = null;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		_result = (DependencyResult) newInput;
	}
	
	private DependencyResult _result;

	@Override
	public Object getSource(Object rel) {
		return ((UniEdge)rel).start();
	}

	@Override
	public Object getDestination(Object rel) {
		return ((UniEdge)rel).end();
	}

	@Override
	public Object[] getElements(Object input) {
		if(_result != null) {
			return _result.dependencies().toArray();
		} else {
			return new Object[0];
		}
	}

}
