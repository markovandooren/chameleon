package org.aikodi.chameleon.ui.widget.tree;

import java.util.List;

import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.workspace.CompositeDocumentScanner;
import org.aikodi.chameleon.workspace.DocumentScanner;

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