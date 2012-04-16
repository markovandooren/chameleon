package chameleon.support.translate;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chameleon.core.compilationunit.Document;
import chameleon.core.language.Language;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespacepart.NamespaceDeclaration;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.exception.ModelException;
import chameleon.plugin.build.BuildProgressHelper;

public abstract class  IncrementalTranslator<S extends Language, T extends Language> {
	
	public IncrementalTranslator(S source, T target) {
		if(source == null || target == null) {
			throw new ChameleonProgrammerException();
		}
		_sourceLanguage = source;
		_targetLanguage = target;
	}
	
	private boolean _initialized=false;

	protected void initTargetLanguage() throws LookupException {
		initTargetLanguage(false);
	}
	protected void initTargetLanguage(boolean force) throws LookupException {
		if ((! _initialized) || force) {
			_implementationMap = new HashMap<Document,Document>();
			Set<Document> compilationUnits = new HashSet<Document>();
			for(NamespaceDeclaration nsp: sourceLanguage().defaultNamespace().descendants(NamespaceDeclaration.class)) {
				Document cu = nsp.nearestAncestor(Document.class);
				if(cu != null) {
					compilationUnits.add(cu);
				}
			}
			for(Document compilationUnit: compilationUnits) {
				implementationCompilationUnit(compilationUnit);
			}
			_initialized=true;
		}
	}
	
	public S sourceLanguage() {
		return _sourceLanguage;
	}
	
	private S _sourceLanguage;
	
	public T targetLanguage() {
		return _targetLanguage;
	}
	
	private T _targetLanguage;
	
	private Map<Document,Document> _implementationMap = new HashMap<Document,Document>();

	public Document implementationCompilationUnit(Document compilationUnit) throws LookupException {
		Document clone = compilationUnit.cloneTo(targetLanguage());
		store(compilationUnit, clone,_implementationMap);
		
		return clone;
	}
	
	protected void store(Document compilationUnit, Document generated) throws LookupException {
		store(compilationUnit, generated, _implementationMap);
	}

	protected void store(Document compilationUnit, Document generated, Map<Document,Document> storage) throws LookupException {
		Document old = storage.get(compilationUnit);
		if(old != null) {
//			if(generated != old) {
//				old.namespacePart(1).getNamespaceLink().unlock();
//			}
			old.disconnect();
		}
		// connect the namespacepart of the clone compilation unit
		// to the proper namespace in the target model. The cloned
		// namespace part is not connected to a namespace, so we
		// need the original namespacepart to obtain the fqn.
		storage.put(compilationUnit, generated);
	}
	
	public abstract Collection<Document> build(Document source, List<Document> allProjectCompilationUnits,	BuildProgressHelper buildProgressHelper) throws ModelException;
}
