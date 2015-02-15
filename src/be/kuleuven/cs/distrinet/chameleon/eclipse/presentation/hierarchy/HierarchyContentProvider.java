/**
 * Created on 23-apr-07
 * @author Tim Vermeiren
 */
package be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.hierarchy;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The Content provider for the hierarchy. This class must be subclassed to
 * implement the SubTypeHierarchy or the SuperTypeHierarchy content provider.
 * 
 * 
 * @author Tim Vermeiren
 */
public abstract class HierarchyContentProvider implements ITreeContentProvider {

	@Override
   public void dispose() {
		// NOP
	}

	@Override
   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// NOP
	}

	@Override
   public boolean hasChildren(Object element) {
		boolean result = getChildren(element).length > 0;
		return result;
	}

	@Override
   public Object[] getElements(Object inputElement) {
		Object[] result = getChildren(inputElement);
		return result;
	}
	
}
