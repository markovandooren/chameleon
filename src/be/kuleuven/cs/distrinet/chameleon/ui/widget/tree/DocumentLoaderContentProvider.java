package be.kuleuven.cs.distrinet.chameleon.ui.widget.tree;

import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.workspace.DocumentLoader;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

/**
 * 
 * @author Marko van Dooren
 */
public class DocumentLoaderContentProvider extends TreeNodeContentProvider<Object> {

//	public LexicalTreeContentProvider() {
//		super(TreeNode.class);
//	}
	
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
	
	public static abstract class LoaderGroupNode extends TreeNode<Project,Object> {

		public LoaderGroupNode(TreeNode<?,Object> parent, Project domainObject, String label) {
			super(parent, domainObject, label);
		}
		
		@Override
		public List<? extends TreeNode<?,Object>> createChildren() {
			Builder<DocumentLoader> sourceBuilder = ImmutableList.<DocumentLoader>builder();
			for(View view:domainObject().views()) {
				sourceBuilder.addAll(loaders(view));
			}
			Builder<DocumentLoaderNode> builder = ImmutableList.builder();
			for(DocumentLoader loader: sourceBuilder.build()) {
				builder.add(new DocumentLoaderNode(this, loader));
			}
			return builder.build();
		}

		protected abstract List<DocumentLoader> loaders(View view);
	}
	
	public static class SourceNode extends LoaderGroupNode {

		public SourceNode(TreeNode<?,Object> parent, Project project) {
			super(parent, project,"Source");
		}

		protected List<DocumentLoader> loaders(View view) {
			return view.sourceLoaders();
		}

	}

	public static class BinaryNode extends LoaderGroupNode {

		public BinaryNode(TreeNode<?,Object> parent, Project project) {
			super(parent, project,"External");
		}

		protected List<DocumentLoader> loaders(View view) {
			return view.binaryLoaders();
		}

	}

	
	

//	@Override
//	public boolean hasChildren(Object element) {
//		if(element instanceof Namespace) {
//			return ((Namespace) element).hasSubNamespaces();
//		} else if(element instanceof DocumentLoader) {
//			return ((DocumentLoader) element).nbInputSources() > 0;
//		}
//		else {
//			return super.hasChildren(element);
//		}
//	}

	@Override
	public Object domainData(TreeNode treeData) {
		return treeData.domainObject();
	}
}
