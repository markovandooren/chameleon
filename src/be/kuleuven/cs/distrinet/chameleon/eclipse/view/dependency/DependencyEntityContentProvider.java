//package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;
//
//import org.eclipse.jface.viewers.ArrayContentProvider;
//import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
//
//import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
//import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
//
//public class DependencyEntityContentProvider extends ArrayContentProvider implements IGraphEntityContentProvider {
//
//	private DependencyResult _dependencyModel;
//	
//	@Override
//	public Object[] getConnectedTo(Object entity) {
//		if(entity instanceof Element) {
//			return _dependencyModel.dependencies((Element) entity).toArray();
//		}
//		else {
//			throw new IllegalArgumentException("Cannot process object of type "+entity.getClass());
//		}
//	}
//	
//	public void setModel(DependencyResult model) {
//		_dependencyModel = model;
//	}
//
//}
