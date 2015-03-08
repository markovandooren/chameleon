package org.aikodi.chameleon.eclipse.nature;

import java.util.Iterator;

import org.aikodi.chameleon.eclipse.project.ChameleonProjectNature;
import org.aikodi.chameleon.eclipse.util.Files;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class AddBackgroundNature extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
        .getActivePage().getSelection();
		if(selection instanceof IStructuredSelection) {
			IStructuredSelection structureSelection = (IStructuredSelection) selection;
			Iterator iterator = structureSelection.iterator();
			while(iterator.hasNext()) {
				try {
				Object current = iterator.next();
				if(current instanceof IProjectNature) {
					current = ((IProjectNature) current).getProject();
				}
				if(current instanceof IProject) {
					IProject project = (IProject) current;
					if(project.exists() && project.isOpen()) {
						IProjectDescription description = project.getDescription();
						IFile projectFile = project.getFile(".project");
						IFile backup = project.getFile(".project.chameleonbackup");
						Files.copy(projectFile, backup);
						String[] natures = description.getNatureIds();
						String[] newNatures = new String[natures.length + 1];
						System.arraycopy(natures, 0, newNatures, 0, natures.length);
						newNatures[natures.length] = ChameleonProjectNature.BACKGROUND_NATURE;
						description.setNatureIds(newNatures);
            // We do not create a Chameleon project file to avoid overwriting a file.
						//
						try {
							project.setDescription(description, new NullProgressMonitor());
						} catch(CoreException exc) {
							Files.copy(backup, projectFile);
						}
					}
				} else {
					System.out.println(current.getClass());
				}
				} catch(CoreException exc) {
					// Because of the check for project.exists() and project.isOpen()
					// the call to project.getDescription() is not allowed to throw
					// a CoreException. Print if it happens.
					exc.printStackTrace();
				}
			}
		}
		return null;
	}
	
}
