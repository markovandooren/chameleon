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

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> children(Object element) {
		if(element instanceof Project) {
			Builder<DocumentLoader> sourceBuilder = ImmutableList.<DocumentLoader>builder();
			Builder<DocumentLoader> binaryBuilder = ImmutableList.<DocumentLoader>builder();
			for(View view:((Project)element).views()) {
				sourceBuilder.addAll(view.sourceLoaders());
				binaryBuilder.addAll(view.binaryLoaders());
			}
			DocumentLoaderNode sourceNode = new DocumentLoaderNode("Project", sourceBuilder.build());
			DocumentLoaderNode binaryNode = new DocumentLoaderNode("External", binaryBuilder.build());
			return ImmutableList.<Object>of(sourceNode,binaryNode);
		} else if(element instanceof DocumentLoader) {
			List<Namespace> namespaces = ((DocumentLoader)element).topLevelNamespaces();
			return (List) namespaces;
		} else if(element instanceof Namespace) {
			List<Namespace> subNamespaces = ((Namespace)element).getSubNamespaces();
			return (List) subNamespaces;
		} else if (element instanceof DocumentLoaderNode){
			return (List)((DocumentLoaderNode) element).documentLoaders();
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
		if(element instanceof NamespaceDeclaration) {
			return ((NamespaceDeclaration)element).namespace();
		} else if(element instanceof Element) {
			return ((Element)element).parent();
		} else if(element instanceof InputSource) {
			return ((InputSource)element).loader();
		} else if(element instanceof DocumentLoader) {
			return ((DocumentLoader)element).project();
		} else {
			return null;
		}
	}

}
