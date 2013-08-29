package be.kuleuven.cs.distrinet.chameleon.ui.widget.tree;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.workspace.CompositeDocumentLoader;
import be.kuleuven.cs.distrinet.chameleon.workspace.DocumentLoader;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class DocumentLoaderNode extends TreeNode<DocumentLoader,Object> {

		public DocumentLoaderNode(TreeNode<?,Object> parent,
				DocumentLoader domainObject) {
			super(parent, domainObject, domainObject.label());
		}

		@Override
		public List<? extends TreeNode<?,Object>> createChildren() {
			if(domainObject() instanceof CompositeDocumentLoader) {
				Builder<DocumentLoaderNode> builder = ImmutableList.builder();
				for(DocumentLoader loader: ((CompositeDocumentLoader)domainObject()).loaders()) {
					builder.add(new DocumentLoaderNode(this, loader));
				}
				return builder.build();
			} else {
				List<Namespace> topLevelNamespaces = domainObject().topLevelNamespaces();
				Builder<NamespaceNode> namespaceBuilder = ImmutableList.builder();
				for(Namespace ns: topLevelNamespaces) {
					namespaceBuilder.add(new NamespaceNode(this, ns));
				}
				return namespaceBuilder.build();
			}

//			else {
//				List<Namespace> topLevelNamespaces = domainObject().topLevelNamespaces();
//				Builder<NamespaceNode> namespaceBuilder = ImmutableList.builder();
//				for(Namespace ns: topLevelNamespaces) {
//					namespaceBuilder.add(new NamespaceNode(this, ns));
//				}
//				return namespaceBuilder.build();
//			}
//			return Collections.EMPTY_LIST;
		}
	}