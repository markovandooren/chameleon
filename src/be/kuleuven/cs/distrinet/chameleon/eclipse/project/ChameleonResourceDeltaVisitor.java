/**
 * 
 */
package be.kuleuven.cs.distrinet.chameleon.eclipse.project;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.eclipse.editors.EclipseDocument;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

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
				  handleChanged(delta);
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
		return doc != null ? doc.document() : null;
	}
	
	public EclipseDocument documentOf(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		IPath path = resource.getFullPath();
		EclipseDocument doc = nature().documentOfPath(path);
		return doc;
	}

}
