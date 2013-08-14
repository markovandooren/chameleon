package be.kuleuven.cs.distrinet.chameleon.eclipse.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;

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

}
