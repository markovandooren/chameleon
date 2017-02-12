/**
 * Created on 23-apr-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.presentation.hierarchy;

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
		  flushCache();
	 }
	
	public abstract void flushCache();

	@Override
   public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	@Override
   public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}
	
}
