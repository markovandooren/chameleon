package chameleon.workspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.document.Document;
import chameleon.core.factory.Factory;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.InputSourceNamespace;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespacedeclaration.NamespaceDeclaration;

public class DirectInputSource extends InputSourceImpl {

	public DirectInputSource(Declaration decl, String namespaceFQN, View view, DocumentLoaderImpl loader) throws InputException {
		if(decl == null) {
			throw new IllegalArgumentException("The given declaration is null.");
		}
		if(namespaceFQN == null) {
			throw new IllegalArgumentException("The given fully qualified name of a namespace is null.");
		}
		if(view == null) {
			throw new IllegalArgumentException("The given view is null.");
		}
		if(loader == null) {
			throw new IllegalArgumentException("The given document loader is null.");
		}
		_declaration = decl;
		InputSourceNamespace ns = (InputSourceNamespace) view.namespace().getOrCreateNamespace(namespaceFQN);
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
		loader.addInputSource(this);
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

	@Override
	protected void doLoad() throws InputException {
		// there is nothing to load, as the element has already been defined.
	}
	
}