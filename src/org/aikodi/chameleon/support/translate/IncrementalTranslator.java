package org.aikodi.chameleon.support.translate;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.plugin.build.BuildException;
import org.aikodi.chameleon.plugin.build.BuildProgressHelper;
import org.aikodi.chameleon.workspace.View;

import java.util.*;

/**
 * A translator that keeps track of a source view and a target view,
 * and can update the target view on a per document basis.
 * 
 * @author Marko van Dooren
 *
 * @param <S> The source language
 * @param <T> The target language
 */
public abstract class IncrementalTranslator<S extends Language, T extends Language> {

  /**
   * Create a new incremental translator that translates documents from the given
   * source view into the given target view.
   * 
   * @param source The source view
   * @param target The target view to which translated documented must be added.
   */
	public IncrementalTranslator(View source, View target) {
		if(source == null || target == null) {
			throw new ChameleonProgrammerException();
		}
		_source = source;
		_target = target;
	}
	
	private boolean _initialized=false;

	
	protected void initTarget() throws LookupException {
		initTarget(false);
	}
	
	protected void initTarget(boolean force) throws LookupException {
		if ((! _initialized) || force) {
			_documentMap = new HashMap<>();
			Set<Document> documents = new HashSet<>();
			for(NamespaceDeclaration nsp: source().namespace().lexical().descendants(NamespaceDeclaration.class)) {
				Document cu = nsp.lexical().nearestAncestor(Document.class);
				if(cu != null) {
					documents.add(cu);
				}
			}
			for(Document compilationUnit: documents) {
				targetDocument(compilationUnit);
			}
			_initialized=true;
		}
	}
	
	/**
	 * @return The view from which documents are translated.
	 */
	public View source() {
		return _source;
	}
	
	private View _source;
	
  /**
   * @return The view to which documents are translated.
   */
	public View target() {
		return _target;
	}
	
	private View _target;
	
	/**
	 * The map that tracks which source document is mapped to which target
	 * document. 
	 */
	private Map<Document,Document> _documentMap = new HashMap<Document,Document>();

	public Document targetDocument(Document source) throws LookupException {
		Document clone = source.cloneTo(target());
		store(source, clone,_documentMap);
		return clone;
	}
	
	protected void store(Document compilationUnit, Document generated) throws LookupException {
		store(compilationUnit, generated, _documentMap);
	}

	protected void store(Document compilationUnit, Document generated, Map<Document,Document> storage) throws LookupException {
		Document old = storage.get(compilationUnit);
		if(old != null) {
			old.disconnect();
		}
		// connect the namespacepart of the clone compilation unit
		// to the proper namespace in the target model. The cloned
		// namespace part is not connected to a namespace, so we
		// need the original namespacepart to obtain the fqn.
		storage.put(compilationUnit, generated);
	}
	
	public abstract Collection<Document> build(Document source, BuildProgressHelper buildProgressHelper) throws BuildException;
}
