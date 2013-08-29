package be.kuleuven.cs.distrinet.chameleon.ui.widget.tree;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class NamespaceNode extends TreeNode<Namespace,Object> {

	public NamespaceNode(TreeNode<?,Object> parent, Namespace domainObject) {
		super(parent, domainObject,domainObject.name());
	}

	@Override
	public List<? extends TreeNode<?,Object>> createChildren() {
		List<Namespace> subNamespaces = domainObject().getSubNamespaces();
		Builder<NamespaceNode> namespaceBuilder = ImmutableList.builder();
		for(Namespace ns: subNamespaces) {
			namespaceBuilder.add(new NamespaceNode(this, ns));
		}
		return namespaceBuilder.build();
	}
}