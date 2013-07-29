package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;

public class DependencyContentProvider extends ArrayContentProvider implements IGraphEntityContentProvider {

	private DependencyResult<Type, Type> _dependencyModel;
	
	@Override
	public Object[] getConnectedTo(Object entity) {
		if(entity instanceof Type) {
			return _dependencyModel.dependencies((Type) entity).toArray();
		}
		else {
			throw new IllegalArgumentException("Cannot process object of type "+entity.getClass());
		}
	}
	
	public void setModel(DependencyResult<Type,Type> model) {
		_dependencyModel = model;
	}

}
