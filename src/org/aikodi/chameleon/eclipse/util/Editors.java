package org.aikodi.chameleon.eclipse.util;

import org.aikodi.chameleon.workspace.Project;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;


public class Editors {

	public static Project chameleonProject(IEditorPart editor) {
			IEditorInput editorInput = editor.getEditorInput();
			if(editorInput instanceof IFileEditorInput) {
				IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
				IProject project = Projects.project(fileEditorInput.getFile().getFullPath());
				Project chameleonProject = Projects.chameleonProject(project);
				return chameleonProject;
			}
		return null;
	}

}
