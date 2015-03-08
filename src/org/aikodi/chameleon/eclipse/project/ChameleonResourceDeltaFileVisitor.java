package org.aikodi.chameleon.eclipse.project;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

public abstract class ChameleonResourceDeltaFileVisitor extends ChameleonResourceDeltaVisitor {

	public ChameleonResourceDeltaFileVisitor(ChameleonProjectNature nature) {
		super(nature);
	}

	/**
	 * Process each resource delta that concerns a file. Other resources
	 * are skipped.
	 */
	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		if(resource.getType() == IResource.FILE) {
			return super.visit(delta);
		} else {
			// We must return true, or else the visitor stops when it reaches
			// the first non-file resource.
			return true;
		}
	}


}
