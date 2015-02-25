package org.aikodi.chameleon.eclipse.util;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

public class Workspaces {

	/**
	 * Return the root of the workspace.
	 * @return
	 */
	public static IWorkspaceRoot root() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	

}
