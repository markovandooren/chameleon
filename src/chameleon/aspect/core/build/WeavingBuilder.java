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
import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.language.Language;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespacepart.NamespacePart;
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
		Set<CompilationUnit> compilationUnits = new HashSet<CompilationUnit>();
		for(NamespacePart nsp: sourceLanguage().defaultNamespace().descendants(NamespacePart.class)) {
			CompilationUnit cu = nsp.nearestAncestor(CompilationUnit.class);
			if(cu != null) {
				compilationUnits.add(cu);
			}
		}
		for(CompilationUnit compilationUnit: compilationUnits) {
			implementationCompilationUnit(compilationUnit);
		}
		
		_initialized = true;
	}
	
	private void buildSingle(CompilationUnit compilationUnit, Collection<CompilationUnit> aspectCompilationUnits) throws LookupException {
		_translator.weave(compilationUnit, aspectCompilationUnits);
	}
	
	public Map<CompilationUnit, CompilationUnit> build(List<CompilationUnit> allProjectCompilationUnits, BuildProgressHelper buildProgressHelper) throws LookupException {
		if (!_initialized)
			initTargetLanguage();
		
		Map<CompilationUnit, CompilationUnit> wovenCompilationUnits = new HashMap<CompilationUnit, CompilationUnit>();
		
		Map<CompilationUnit, CompilationUnit> aspects = new HashMap<CompilationUnit, CompilationUnit>();
		Map<CompilationUnit, CompilationUnit> other = new HashMap<CompilationUnit, CompilationUnit>();
		
		// First rebuild aspects
		for (CompilationUnit originalCompilationUnit : allProjectCompilationUnits) {
			CompilationUnit clonedCompilationUnit = implementationCompilationUnit(originalCompilationUnit);
			
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
		for (Entry<CompilationUnit, CompilationUnit> cu : other.entrySet()) {
			System.out.println("Weaving: " + ++i);
			buildProgressHelper.checkForCancellation();
			buildSingle(cu.getValue(), aspects.values());
			buildProgressHelper.addWorked(1);
		}
		
		buildProgressHelper.addWorked(aspects.size());
		
		System.out.println("Rebuilt " + wovenCompilationUnits.size() + " compilationUnit(s)");
		
		return wovenCompilationUnits;
	}
	
	private Map<CompilationUnit,CompilationUnit> _implementationMap = new HashMap<CompilationUnit,CompilationUnit>();
	
	public CompilationUnit implementationCompilationUnit(CompilationUnit compilationUnit) throws LookupException {
		CompilationUnit clone = compilationUnit.cloneTo(targetLanguage());
		store(compilationUnit, clone,_implementationMap);
   
		return clone;
	}
	
	private void store(CompilationUnit compilationUnit, CompilationUnit generated, Map<CompilationUnit,CompilationUnit> storage) throws LookupException {
		CompilationUnit old = storage.get(compilationUnit);
		if(old != null) {
			if(generated != old) {
				old.namespacePart(1).getNamespaceLink().unlock();
			}
			old.disconnect();
		}
		// connect the namespacepart of the clone compilation unit
		// to the proper namespace in the target model. The cloned
		// namespace part is not connected to a namespace, so we
		// need the original namespacepart to obtain the fqn.
		storage.put(compilationUnit, generated);
	}
}
