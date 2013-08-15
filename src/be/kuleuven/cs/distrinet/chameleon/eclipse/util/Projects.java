package be.kuleuven.cs.distrinet.chameleon.eclipse.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import be.kuleuven.cs.distrinet.chameleon.eclipse.project.ChameleonProjectNature;

public class Projects {

	public static boolean hasNature(IProject project, String nature) {
		try {
			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();
			for(int i =0; i < natures.length;i++) {
				if(nature.equals(natures[i])) {
					return true;
				}
			}
			return false;
		} catch (CoreException e) {
			return false;
		}
	}

	public static ChameleonProjectNature chameleonNature(IProject project) {
		try {
			ChameleonProjectNature result = (ChameleonProjectNature) project.getNature(ChameleonProjectNature.NATURE);
			if(result == null) {
				result = (ChameleonProjectNature) project.getNature(ChameleonProjectNature.BACKGROUND_NATURE);
			}
			return result;
		} catch (CoreException e) {
			return null;
		}
	}
	
	public static IProject project(IPath path) {
		return Workspaces.root().findMember(path).getProject();
	}


}
