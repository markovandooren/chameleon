/**
 * Created on 13-jun-07
 * @author Tim Vermeiren
 */
package be.kuleuven.cs.distrinet.chameleon.eclipse.view.callhierarchy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.eclipse.project.ChameleonProjectNature;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.util.Handler;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputException;
import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;

/**
 * A context provider that calculates all the declarations that contain
 * a cross-reference to a given declaration.
 * 
 * @author Tim Vermeiren
 */
public class CallersContentProvider implements ITreeContentProvider {
	
	private ChameleonProjectNature projectNature;
	
	public CallersContentProvider(ChameleonProjectNature projectNature) {
		this.projectNature = projectNature;
	}
	
	/**
	 * Returns all the declarations that contain a cross-reference to the given declaration 
	 * (if inputObject is a Declaration)
	 */
	@Override
   public Object[] getChildren(Object inputObject) {
		if(inputObject instanceof Declaration){
			final Declaration declaration = (Declaration)inputObject;
			// get all invocations in this project:
			Collection<CrossReference> invocations = getInvocations();
			// System.out.println("found "+ invocations.size()+" invocations");
			// build a predicate to search for matching invocations:
			SafePredicate<CrossReference> predicate = new SafePredicate<CrossReference>(){
				@Override
				public boolean eval(CrossReference invocation) {
					try {
						return declaration.equals(invocation.getElement().declarator());
					} catch (ModelException e) {
						e.printStackTrace();
						return false;
					}
				}
			};
			// filter invocations
			predicate.filter(invocations);
			// get the methods containing the invocations:
			Collection<Declaration> result = new HashSet<Declaration>();
			for(CrossReference invocation : invocations){
				Declaration callingDeclaration = invocation.nearestAncestor(Declaration.class);
				if(callingDeclaration != null) {
					result.add(callingDeclaration);
				}
			}
			return result.toArray();
		} else if(inputObject instanceof RootDeclaration){
			Declaration declaration = ((RootDeclaration)inputObject).getDeclaration();
			return new Object[]{declaration};
		}
		return null;
	}
	
	/**
	 * Cashing of the method invocations of this project (for performance reasons)
	 */
	private Collection<CrossReference> _cachedInvocations;
	
	/**
	 * Returns all the method invocations in the current project.
	 * 
	 */
	private Collection<CrossReference> getInvocations() {
		List<CrossReference> result = new ArrayList<CrossReference>();
		if(_cachedInvocations == null){
			try {
				_cachedInvocations = projectNature.chameleonProject().sourceElements(CrossReference.class, Handler.IGNORE);
			} catch (InputException e) {
				// We pass IGNORE, so this really shouldn't happen
				throw new Error(e);
			}
		}
		return new HashSet<CrossReference>(_cachedInvocations);
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
		// return new Object[]{inputObject}; 
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
