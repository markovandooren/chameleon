/**
 * Created on 25-apr-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.presentation.hierarchy;

import org.aikodi.chameleon.oo.type.Type;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * This class calculates the members of a given type. 
 * Needed to populate the member viewer of the hierarchy view.
 * 
 * @author Tim Vermeiren
 * @author Marko van Dooren
 */
public class MemberContentProvider implements IStructuredContentProvider {
	
	/**
	 * If inputElement is a Type, the direct members are returned
	 */
	@Override
   public Object[] getElements(Object inputElement) {
//		try {
//			throw new Exception("MEMBER CONTENT PROVIDER GET ELEMENTS");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		if(inputElement instanceof Type){
			Type type = (Type)inputElement;
				return type.directlyDeclaredMembers().toArray();
		}
		return null;
	}

	@Override
   public void dispose() {
		
	}

	@Override
   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		System.out.println("INPUT CHANGED!!!!:   old: " + oldInput +"new: "+newInput);
	}

}
