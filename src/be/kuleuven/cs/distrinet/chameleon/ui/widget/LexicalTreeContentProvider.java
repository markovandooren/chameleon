package be.kuleuven.cs.distrinet.chameleon.ui.widget;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.workspace.CompositeDocumentLoader;
import be.kuleuven.cs.distrinet.chameleon.workspace.DocumentLoader;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

/**
 * 
 * @author Marko van Dooren
 */
public class LexicalTreeContentProvider extends TreeContentProvider<Object> {

//	public LexicalTreeContentProvider() {
//		super(TreeNode.class);
//	}
	
	@Override
	public TreeNode<? extends Object> createNode(Object input) {
		if(input instanceof Project) {
			return new ProjectNode((Project) input);
		} else {
			throw new IllegalArgumentException("Cannot create a lexical tree content provider root node.");
		}
	}
	
	public static class ProjectNode extends TreeNode<Project> {

		public ProjectNode(Project domainObject) {
			super(domainObject, domainObject.getName());
		}

		@Override
		public List<? extends TreeNode<?>> createChildren() {
			return ImmutableList.<TreeNode<Project>>of(
					new SourceNode(this, domainObject()), 
					new BinaryNode(this, domainObject()));
		}
		
	}
	
	public static abstract class LoaderGroupNode extends TreeNode<Project> {

		public LoaderGroupNode(TreeNode<?> parent, Project domainObject, String label) {
			super(parent, domainObject, label);
		}
		
		@Override
		public List<? extends TreeNode<?>> createChildren() {
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

		public SourceNode(TreeNode<?> parent, Project project) {
			super(parent, project,"Source");
		}

		protected List<DocumentLoader> loaders(View view) {
			return view.sourceLoaders();
		}

	}

	public static class BinaryNode extends LoaderGroupNode {

		public BinaryNode(TreeNode<?> parent, Project project) {
			super(parent, project,"External");
		}

		protected List<DocumentLoader> loaders(View view) {
			return view.binaryLoaders();
		}

	}

	public static class DocumentLoaderNode extends TreeNode<DocumentLoader> {

		public DocumentLoaderNode(TreeNode<?> parent,
				DocumentLoader domainObject) {
			super(parent, domainObject, domainObject.label());
		}

		@Override
		public List<? extends TreeNode<?>> createChildren() {
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
		}
	}
	
	public static class NamespaceNode extends TreeNode<Namespace> {

		public NamespaceNode(TreeNode<?> parent, Namespace domainObject) {
			super(parent, domainObject,domainObject.name());
		}

		@Override
		public List<? extends TreeNode<?>> createChildren() {
			List<Namespace> subNamespaces = domainObject().getSubNamespaces();
			Builder<NamespaceNode> namespaceBuilder = ImmutableList.builder();
			for(Namespace ns: subNamespaces) {
				namespaceBuilder.add(new NamespaceNode(this, ns));
			}
			return namespaceBuilder.build();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TreeNode<Object>> children(TreeNode element) {
			return ((TreeNode) element).children();
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
	public TreeNode parent(TreeNode element) {
			return ((TreeNode) element).parent();
	}

	@Override
	public Object domainData(TreeNode treeData) {
		return treeData.domainObject();
	}
}
