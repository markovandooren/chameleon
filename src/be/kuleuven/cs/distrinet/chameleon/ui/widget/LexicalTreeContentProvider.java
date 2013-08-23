package be.kuleuven.cs.distrinet.chameleon.ui.widget;

import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.workspace.DocumentLoader;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputSource;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class LexicalTreeContentProvider extends TreeContentProvider<Object> {

	public LexicalTreeContentProvider() {
		super(Object.class);
	}
	
	@Override
	public TreeViewerNode<?> createNode(Object input) {
		if(input instanceof Project) {
			return new ProjectNode((Project) input);
		} else {
			throw new IllegalArgumentException("Cannot create a lexical tree content provider root node.");
		}
	}
	
	private static class ProjectNode extends TreeViewerNode<Project> {

		public ProjectNode(Project domainObject) {
			super(domainObject, domainObject.getName());
		}

		@Override
		public List<?> children() {
			return ImmutableList.<TreeViewerNode<Project>>of(
					new SourceNode(this, domainObject()), 
					new BinaryNode(this, domainObject()));
		}
		
	}
	
	private static abstract class LoaderGroupNode extends TreeViewerNode<Project> {

		public LoaderGroupNode(TreeViewerNode<?> parent, Project domainObject, String label) {
			super(parent, domainObject, label);
		}
		
		@Override
		public List<?> children() {
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
	
	private static class SourceNode extends LoaderGroupNode {

		public SourceNode(TreeViewerNode<?> parent, Project project) {
			super(parent, project,"Sources");
		}

		protected List<DocumentLoader> loaders(View view) {
			return view.sourceLoaders();
		}

	}

	private static class BinaryNode extends LoaderGroupNode {

		public BinaryNode(TreeViewerNode<?> parent, Project project) {
			super(parent, project,"Binaries");
		}

		protected List<DocumentLoader> loaders(View view) {
			return view.binaryLoaders();
		}

	}

	private static class DocumentLoaderNode extends TreeViewerNode<DocumentLoader> {

		public DocumentLoaderNode(TreeViewerNode<?> parent,
				DocumentLoader domainObject) {
			super(parent, domainObject, domainObject.label());
		}

		@Override
		public List<?> children() {
			List<Namespace> topLevelNamespaces = domainObject().topLevelNamespaces();
			Builder<NamespaceNode> namespaceBuilder = ImmutableList.builder();
			for(Namespace ns: topLevelNamespaces) {
				namespaceBuilder.add(new NamespaceNode(this, ns));
			}
			return namespaceBuilder.build();
		}
	}
	
	private static class NamespaceNode extends TreeViewerNode<Namespace> {

		public NamespaceNode(TreeViewerNode<?> parent, Namespace domainObject) {
			super(parent, domainObject,domainObject.name());
		}

		@Override
		public List<?> children() {
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
	public List<Object> children(Object element) {
		if(element instanceof TreeViewerNode) {
			return ((TreeViewerNode) element).children();
		} 
		else {
			return Collections.EMPTY_LIST;
		}
	}
	
	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof Namespace) {
			return ((Namespace) element).hasSubNamespaces();
		} else if(element instanceof DocumentLoader) {
			return ((DocumentLoader) element).nbInputSources() > 0;
		}
		else {
			return super.hasChildren(element);
		}
	}

	@Override
	public Object parent(Object element) {
		if(element instanceof TreeViewerNode) {
			return ((TreeViewerNode) element).parent();
		} else {
			return null;
		}
	}

}
