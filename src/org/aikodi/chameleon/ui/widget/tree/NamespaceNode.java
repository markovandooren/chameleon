package org.aikodi.chameleon.ui.widget.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import org.aikodi.chameleon.core.namespace.Namespace;

import java.util.List;

public class NamespaceNode extends TreeNode<Namespace,Object> {

	public NamespaceNode(TreeNode<?,Object> parent, Namespace domainObject) {
		super(parent, domainObject,domainObject.name());
	}

	@Override
	public List<? extends TreeNode<?,Object>> createChildren() {
		List<Namespace> subNamespaces = domainObject().subNamespaces();
		Builder<NamespaceNode> namespaceBuilder = ImmutableList.builder();
		for(Namespace ns: subNamespaces) {
			namespaceBuilder.add(new NamespaceNode(this, ns));
		}
		return namespaceBuilder.build();
	}
}