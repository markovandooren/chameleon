package be.kuleuven.cs.distrinet.chameleon.aspect.core.build;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.aspect.Aspect;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.weave.AspectWeaver;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.plugin.build.BuildProgressHelper;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;

//TODO extend van IncrementalJavaTranslator
public class WeavingBuilder<S extends Language, T extends Language> {

	public WeavingBuilder(View source, View target, AspectWeaver translator) {
		_source = source;
		_target = target;
		_translator = translator;
	}
	
	private AspectWeaver _translator;

	public View source() {
		return _source;
	}
	
	private View _source;
	
	public View target() {
		return _target;
	}
	
	private View _target;
	
	private boolean _initialized = false;	
	
	private void initTargetLanguage() throws LookupException {
		Set<Document> compilationUnits = new HashSet<Document>();
		for(NamespaceDeclaration nsp: source().namespace().descendants(NamespaceDeclaration.class)) {
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
		Document clone = compilationUnit.cloneTo(target());
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
