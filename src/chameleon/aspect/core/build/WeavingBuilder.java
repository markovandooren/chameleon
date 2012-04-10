package chameleon.aspect.core.build;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import chameleon.aspect.core.model.aspect.Aspect;
import chameleon.aspect.core.weave.AspectWeaver;
import chameleon.core.compilationunit.Document;
import chameleon.core.language.Language;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespacepart.NamespaceDeclaration;
import chameleon.plugin.build.BuildProgressHelper;

//TODO extend van IncrementalJavaTranslator
public class WeavingBuilder<S extends Language, T extends Language> {

	public WeavingBuilder(S source, T target, AspectWeaver translator) {
		_sourceLanguage = source;
		_targetLanguage = target;
		_translator = translator;
	}
	
	private AspectWeaver _translator;

	public S sourceLanguage() {
		return _sourceLanguage;
	}
	
	private S _sourceLanguage;
	
	public T targetLanguage() {
		return _targetLanguage;
	}
	
	private T _targetLanguage;
	
	private boolean _initialized = false;	
	
	private void initTargetLanguage() throws LookupException {
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
		
		_initialized = true;
	}
	
	private void buildSingle(Document compilationUnit, Collection<Document> aspectCompilationUnits) throws LookupException {
		_translator.weave(compilationUnit, aspectCompilationUnits);
	}
	
	public Map<Document, Document> build(List<Document> allProjectCompilationUnits, BuildProgressHelper buildProgressHelper) throws LookupException {
		if (!_initialized)
			initTargetLanguage();
		
		Map<Document, Document> wovenCompilationUnits = new HashMap<Document, Document>();
		
		Map<Document, Document> aspects = new HashMap<Document, Document>();
		Map<Document, Document> other = new HashMap<Document, Document>();
		
		// First rebuild aspects
		for (Document originalCompilationUnit : allProjectCompilationUnits) {
			Document clonedCompilationUnit = implementationCompilationUnit(originalCompilationUnit);
			
			if (originalCompilationUnit.hasDescendant(Aspect.class)) {
				aspects.put(originalCompilationUnit, clonedCompilationUnit);
			}
			else {
				other.put(originalCompilationUnit, clonedCompilationUnit);
			}
		}
				
		wovenCompilationUnits.putAll(aspects);
		wovenCompilationUnits.putAll(other);
		int i = 0;
		// Next two loops are the same but the order is important, so don't merge them just yet
		for (Entry<Document, Document> cu : other.entrySet()) {
			System.out.println("Weaving: " + ++i);
			buildProgressHelper.checkForCancellation();
			buildSingle(cu.getValue(), aspects.values());
			buildProgressHelper.addWorked(1);
		}
		
		buildProgressHelper.addWorked(aspects.size());
		
		System.out.println("Rebuilt " + wovenCompilationUnits.size() + " compilationUnit(s)");
		
		return wovenCompilationUnits;
	}
	
	private Map<Document,Document> _implementationMap = new HashMap<Document,Document>();
	
	public Document implementationCompilationUnit(Document compilationUnit) throws LookupException {
		Document clone = compilationUnit.cloneTo(targetLanguage());
		store(compilationUnit, clone,_implementationMap);
   
		return clone;
	}
	
	private void store(Document compilationUnit, Document generated, Map<Document,Document> storage) throws LookupException {
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
}
