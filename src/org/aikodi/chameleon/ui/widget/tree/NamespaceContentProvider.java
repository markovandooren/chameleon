package org.aikodi.chameleon.ui.widget.tree;

import org.aikodi.chameleon.workspace.Project;

public class NamespaceContentProvider extends TreeNodeContentProvider<Object> {

	@Override
	public TreeNode<?,Object> createNode(Object input) {
		if(input instanceof Project) {
			Project project = (Project) input;
			return new NamespaceNode(null, project.views().get(0).namespace());
		} else {
			throw new IllegalArgumentException("Cannot create a lexical tree content provider root node.");
		}
	}

}
