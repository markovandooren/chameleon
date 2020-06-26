/**
 * @author Tim Vermeiren
 * @author Marko van Dooren
 */
package org.aikodi.chameleon.eclipse.view.callhierarchy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.eclipse.project.ChameleonProjectNature;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.rejuse.exception.Handler;
import org.aikodi.rejuse.predicate.SafePredicate;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * A context provider that calculates all the declarations that contain a
 * cross-reference to a given declaration.
 * 
 * @author Tim Vermeiren
 */
public class CallersContentProvider implements ITreeContentProvider {

	private ChameleonProjectNature projectNature;

	public CallersContentProvider(ChameleonProjectNature projectNature) {
		this.projectNature = projectNature;
	}

	/**
	 * Returns all the declarations that contain a cross-reference to the given
	 * declaration (if inputObject is a Declaration)
	 */
	@Override
	public Object[] getChildren(Object inputObject) {
		if (inputObject instanceof Declaration) {
			final Declaration declaration = (Declaration) inputObject;
			// get all invocations in this project:
			Collection<CrossReference> invocations = getInvocations();
			// System.out.println("found "+ invocations.size()+" invocations");
			// build a predicate to search for matching invocations:
			SafePredicate<CrossReference> predicate = new SafePredicate<CrossReference>() {
				@Override
				public boolean eval(CrossReference invocation) {
					try {
						Declaration referenced = invocation.getElement();
						Declaration declarator = referenced.declarator();
						boolean found = false;
						if (declarator.farthestOrigin()
								.sameAs(declaration.farthestOrigin())) {
							found = true;
						} else {
							Set<? extends Declaration> overriddenDeclarations = declarator
									.overriddenDeclarations();
							for (Declaration overridden : overriddenDeclarations) {
								found = found || overridden.farthestOrigin()
										.sameAs(declaration.farthestOrigin());
							}
						}
						return found;
					} catch (ModelException e) {
						return false;
					}
				}
			};
			// filter invocations
			predicate.filter(invocations);
			// get the methods containing the invocations:
			Collection<Declaration> result = new HashSet<Declaration>();
			for (CrossReference invocation : invocations) {
				Declaration callingDeclaration = invocation.lexical()
						.nearestAncestor(Declaration.class);
				if (callingDeclaration != null) {
					result.add(callingDeclaration);
				}
			}
			return result.toArray();
		} else if (inputObject instanceof RootDeclaration) {
			Declaration declaration = ((RootDeclaration) inputObject)
					.getDeclaration();
			return new Object[] { declaration };
		}
		return null;
	}

	/**
	 * Caching of the method invocations of this project (for performance reasons)
	 */
	@SuppressWarnings("rawtypes")
	// We need to suppress the warning because we use the class literal further on.
	private Collection<CrossReference> _cachedInvocations;

	/**
	 * Returns all the method invocations in the current project.
	 * 
	 */
	private Collection<CrossReference> getInvocations() {
		List<CrossReference<?>> result = new ArrayList<>();
		if (_cachedInvocations == null) {
			_cachedInvocations = projectNature.chameleonProject()
					.sourceElements(CrossReference.class, Handler.resume());
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
