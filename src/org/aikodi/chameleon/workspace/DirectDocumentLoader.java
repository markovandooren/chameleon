package org.aikodi.chameleon.workspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.factory.Factory;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.DocumentLoaderNamespace;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;

/**
 * A class of document loaders that load a document directly.
 *  
 * @author Marko van Dooren
 */
public class DirectDocumentLoader extends DocumentLoaderImpl {

	public DirectDocumentLoader(Declaration decl, String namespaceFQN, View view, DocumentScanner scanner) throws InputException {
		init(scanner);

		if(decl == null) {
			throw new IllegalArgumentException("The given declaration is null.");
		}
		if(namespaceFQN == null) {
			throw new IllegalArgumentException("The given fully qualified name of a namespace is null.");
		}
		if(view == null) {
			throw new IllegalArgumentException("The given view is null.");
		}
		_declaration = decl;
		DocumentLoaderNamespace ns = (DocumentLoaderNamespace) view.namespace().getOrCreateNamespace(namespaceFQN);
		setNamespace(ns);
		Factory plugin = view.language().plugin(Factory.class);
		NamespaceDeclaration nsd;
		if(! "".equals(namespaceFQN)) {
			nsd = plugin.createNamespaceDeclaration(namespaceFQN);
		} else {
			nsd = plugin.createRootNamespaceDeclaration();
		}
		nsd.add(decl);
		Document doc = new Document();
		doc.add(nsd);
		setDocument(doc);
		doc.activate();
	}
	
	private Declaration _declaration;
	
	public Declaration declaration() {
		return _declaration;
	}
	
	@Override
	public List<String> targetDeclarationNames(Namespace ns) {
		return Collections.singletonList(declaration().name());
	}

	@Override
	public List<Declaration> targetDeclarations(String name) throws LookupException {
		Declaration decl = declaration();
		List<Declaration> result;
		if(decl.name().equals(name)) {
			result = new ArrayList<Declaration>(1);
			result.add(decl);
		} else {
			result = Collections.EMPTY_LIST;
		}
		return result;
	}

	/**
	 * We don't do an actual refresh since the contents of the document loader
	 * was set directly.
	 */
	@Override
	public void doRefresh() throws InputException {
	}
}
