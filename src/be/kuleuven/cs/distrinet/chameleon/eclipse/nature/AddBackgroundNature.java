package be.kuleuven.cs.distrinet.chameleon.eclipse.nature;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import be.kuleuven.cs.distrinet.chameleon.eclipse.project.ChameleonProjectNature;
import be.kuleuven.cs.distrinet.chameleon.eclipse.util.Files;

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
						// We must create the Chameleon project file before we change the project
						// description because the latter will trigger the creation of the
						// chameleon project nature.
						try {
							project.setDescription(description, new NullProgressMonitor());
						} catch(CoreException exc) {
							Files.copy(backup, projectFile);
						}
					}
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
