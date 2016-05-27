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
			Builder<TreeNode<?,Object>> builder = ImmutableList.builder();
			if(domainObject() instanceof CompositeDocumentScanner) {
				for(DocumentScanner scanner: ((CompositeDocumentScanner)domainObject()).scanners()) {
					builder.add(new DocumentScannerNode(this, scanner));
				}
				return builder.build();
			} else {
				List<Namespace> topLevelNamespaces = domainObject().topLevelNamespaces();
				for(Namespace ns: topLevelNamespaces) {
					builder.add(new NamespaceNode(this, ns));
				}
				return builder.build();
			}
		}
	}