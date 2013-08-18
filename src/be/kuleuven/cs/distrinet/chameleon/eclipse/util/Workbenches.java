package be.kuleuven.cs.distrinet.chameleon.eclipse.util;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

import be.kuleuven.cs.distrinet.chameleon.workspace.Project;

public class Workbenches {

	public static Project chameleonProject(IWorkbenchPart part) {
		if(part instanceof IEditorPart) {
			return Editors.chameleonProject((IEditorPart) part);
		}
		return null;
	}

}
