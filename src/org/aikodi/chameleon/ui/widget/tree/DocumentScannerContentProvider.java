package org.aikodi.chameleon.ui.widget.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import org.aikodi.chameleon.workspace.DocumentScanner;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.chameleon.workspace.View;

import java.util.List;

/**
 * 
 * @author Marko van Dooren
 */
public class DocumentScannerContentProvider extends TreeNodeContentProvider<Object> {

	@Override
	public TreeNode<?, Object> createNode(Object input) {
		if(input instanceof Project) {
			return new ProjectNode((Project) input);
		} else {
			throw new IllegalArgumentException("Cannot create a lexical tree content provider root node.");
		}
	}
	
	public static class ProjectNode extends TreeNode<Project,Object> {

		public ProjectNode(Project domainObject) {
			super(domainObject, domainObject.getName());
		}

		@Override
		public List<? extends TreeNode<?,Object>> createChildren() {
			return ImmutableList.<TreeNode<Project,Object>>of(
					new SourceNode(this, domainObject()), 
					new BinaryNode(this, domainObject()));
		}
		
	}
	
	public static abstract class ScannerGroupNode extends TreeNode<Project,Object> {

		public ScannerGroupNode(TreeNode<?,Object> parent, Project domainObject, String label) {
			super(parent, domainObject, label);
		}
		
		@Override
		public List<? extends TreeNode<?,Object>> createChildren() {
			Builder<DocumentScanner> sourceBuilder = ImmutableList.<DocumentScanner>builder();
			for(View view:domainObject().views()) {
				sourceBuilder.addAll(scanners(view));
			}
			Builder<DocumentScannerNode> builder = ImmutableList.builder();
			for(DocumentScanner scanner: sourceBuilder.build()) {
				builder.add(new DocumentScannerNode(this, scanner));
			}
			return builder.build();
		}

		protected abstract List<DocumentScanner> scanners(View view);
	}
	
	public static class SourceNode extends ScannerGroupNode {

		public SourceNode(TreeNode<?,Object> parent, Project project) {
			super(parent, project,"Source");
		}

		@Override
      protected List<DocumentScanner> scanners(View view) {
			return view.sourceScanners();
		}

	}

	public static class BinaryNode extends ScannerGroupNode {

		public BinaryNode(TreeNode<?,Object> parent, Project project) {
			super(parent, project,"External");
		}

		@Override
      protected List<DocumentScanner> scanners(View view) {
			return view.binaryScanners();
		}

	}

	@Override
	public Object domainData(TreeNode treeData) {
		return treeData.domainObject();
	}
}
