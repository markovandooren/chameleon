package org.aikodi.chameleon.eclipse.view.callhierarchy;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.exception.ModelException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Calculates all the methods that are called by a given method
 * 
 * @author Tim Vermeiren
 * @author Marko van Dooren
 */
public class CalleesContentProvider implements ITreeContentProvider {

	/**
	 * Calculates all the methods that are called by a given method
	 */
	@Override
   public Object[] getChildren(Object inputObject) {
		if (inputObject instanceof Declaration) {
			Declaration method = (Declaration) inputObject;
			// get all the invocations of the given method:
			List<CrossReference> invocations = method.lexical().descendants(CrossReference.class);
			// get all the methods of these invocations:
			final Set<Declaration> referencedDeclarations = new HashSet<Declaration>();
			for(CrossReference<?> cref: invocations) {
				try {
					referencedDeclarations.add(cref.getElement());
				} catch (ModelException e) {
				}
			}
			// return result:
			return referencedDeclarations.toArray();
		} else if(inputObject instanceof RootDeclaration){
			Declaration declaration = ((RootDeclaration)inputObject).getDeclaration();
			return new Object[]{declaration};
		}
		return null;
	}

	@Override
   public Object getParent(Object inputObject) {
		return null;
	}

	@Override
   public boolean hasChildren(Object inputObject) {
		return true;
	}

	@Override
   public Object[] getElements(Object inputObject) {
		return getChildren(inputObject);
	}

	@Override
   public void dispose() {
		// NOP
	}

	@Override
   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// NOP
	}

}
