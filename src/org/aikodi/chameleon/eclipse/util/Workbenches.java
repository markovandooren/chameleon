package org.aikodi.chameleon.eclipse.util;

import org.aikodi.chameleon.workspace.Project;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

public class Workbenches {

	public static Project chameleonProject(IWorkbenchPart part) {
		if(part instanceof IEditorPart) {
			return Editors.chameleonProject((IEditorPart) part);
		}
		return null;
	}

}
