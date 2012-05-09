package chameleon.eclipse.presentation.callhierarchy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import chameleon.core.declaration.Declaration;
import chameleon.core.reference.CrossReference;
import chameleon.exception.ModelException;

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
	public Object[] getChildren(Object inputObject) {
		if (inputObject instanceof Declaration) {
			Declaration method = (Declaration) inputObject;
			// get all the invocations of the given method:
			List<CrossReference> invocations = method.descendants(CrossReference.class);
			// get all the methods of these invocations:
			final Set<Declaration> referencedDeclarations = new HashSet<Declaration>();
			for(CrossReference<?> cref: invocations) {
				try {
					referencedDeclarations.add(cref.getElement());
				} catch (ModelException e) {
					e.printStackTrace();
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

	public Object getParent(Object inputObject) {
		return null;
	}

	public boolean hasChildren(Object inputObject) {
		return true;
	}

	public Object[] getElements(Object inputObject) {
		return getChildren(inputObject);
	}

	public void dispose() {
		// NOP
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// NOP
	}

}
