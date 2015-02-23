package be.kuleuven.cs.distrinet.chameleon.ui.widget.tree;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.workspace.CompositeDocumentScanner;
import be.kuleuven.cs.distrinet.chameleon.workspace.DocumentScanner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class DocumentScannerNode extends TreeNode<DocumentScanner,Object> {

		public DocumentScannerNode(TreeNode<?,Object> parent,
				DocumentScanner domainObject) {
			super(parent, domainObject, domainObject.label());
		}

		@Override
		public List<? extends TreeNode<?,Object>> createChildren() {
			if(domainObject() instanceof CompositeDocumentScanner) {
				Builder<DocumentScannerNode> builder = ImmutableList.builder();
				for(DocumentScanner scanner: ((CompositeDocumentScanner)domainObject()).scanners()) {
					builder.add(new DocumentScannerNode(this, scanner));
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
		}
	}