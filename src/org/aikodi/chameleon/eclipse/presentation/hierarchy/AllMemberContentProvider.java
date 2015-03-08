/**
 * Created on 25-apr-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.presentation.hierarchy;

import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.type.Type;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * returns all members (also inherited) of a type
 * 
 * @author Tim Vermeiren
 *
 */
public class AllMemberContentProvider implements IStructuredContentProvider {
	
	/**
	 * If the inputElement is a type, all the (direct and inherited) member
	 * are returned.
	 */
	@Override
   public Object[] getElements(Object inputElement) {
		try {
			if(inputElement instanceof Type){
				Type type = (Type)inputElement;
				return type.members().toArray();
			}
		} catch (ModelException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
   public void dispose() {
		
	}

	@Override
   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

}
