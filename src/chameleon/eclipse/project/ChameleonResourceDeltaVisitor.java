/**
 * 
 */
package chameleon.eclipse.project;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import chameleon.core.document.Document;
import chameleon.eclipse.editors.EclipseDocument;

public abstract class ChameleonResourceDeltaVisitor implements IResourceDeltaVisitor {
	
	public ChameleonResourceDeltaVisitor(ChameleonProjectNature nature) {
		_nature = nature;
	}
	
	private ChameleonProjectNature _nature;
	
	public ChameleonProjectNature nature() {
		return _nature;
	}
	
	public boolean visit(IResourceDelta delta) throws CoreException {
			switch (delta.getKind()) {
			case IResourceDelta.ADDED :
				handleAdded(delta);
				break;
			case IResourceDelta.REMOVED :
				handleRemoved(delta);
				break;
			case IResourceDelta.CHANGED :
				int flags = delta.getFlags();
				if(! ((flags & IResourceDelta.MARKERS) == IResourceDelta.MARKERS)) {
				  handleChanged(delta);
				}
				break;
			}
		return true;
	}
	
	public abstract void handleAdded(IResourceDelta delta) throws CoreException;

	public abstract void handleRemoved(IResourceDelta delta) throws CoreException;

	public abstract void handleChanged(IResourceDelta delta) throws CoreException;

	public Document chameleonDocumentOf(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		IPath path = resource.getFullPath();
		EclipseDocument doc = nature().documentOfPath(path);
		return doc.document();
	}
	
	public EclipseDocument documentOf(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		IPath path = resource.getFullPath();
		EclipseDocument doc = nature().documentOfPath(path);
		return doc;
	}

}