package chameleon.support.translate;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.language.Language;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespacepart.NamespacePart;
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
			_implementationMap = new HashMap<CompilationUnit,CompilationUnit>();
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
	
	private Map<CompilationUnit,CompilationUnit> _implementationMap = new HashMap<CompilationUnit,CompilationUnit>();

	public CompilationUnit implementationCompilationUnit(CompilationUnit compilationUnit) throws LookupException {
		CompilationUnit clone = compilationUnit.cloneTo(targetLanguage());
		store(compilationUnit, clone,_implementationMap);
		
		return clone;
	}
	
	protected void store(CompilationUnit compilationUnit, CompilationUnit generated) throws LookupException {
		store(compilationUnit, generated, _implementationMap);
	}

	protected void store(CompilationUnit compilationUnit, CompilationUnit generated, Map<CompilationUnit,CompilationUnit> storage) throws LookupException {
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
	
	public abstract Collection<CompilationUnit> build(CompilationUnit source, List<CompilationUnit> allProjectCompilationUnits,	BuildProgressHelper buildProgressHelper) throws ModelException;
}
