package chameleon.aspect.core.build;

import java.util.ArrayList;
import java.util.List;

import chameleon.aspect.core.model.aspect.Aspect;
import chameleon.aspect.core.model.language.AspectOrientedLanguage;
import chameleon.aspect.core.weave.AspectWeaver;
import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.language.Language;
import chameleon.core.lookup.LookupException;
import chameleon.plugin.build.BuildProgressHelper;
import chameleon.support.translate.IncrementalTranslator;

public class IncrementalAspectTranslator extends IncrementalTranslator<AspectOrientedLanguage, Language> {

	public IncrementalAspectTranslator(AspectOrientedLanguage source, Language target, AspectWeaver weaver) {
		super(source, target);
		_translator = weaver;
	}
	
	private AspectWeaver _translator;
	
	public AspectWeaver basicTranslator() {
		return _translator;
	}
	
	public List<CompilationUnit> build(CompilationUnit dummy, List<CompilationUnit> allProjectCompilationUnits, BuildProgressHelper buildProgressHelper) throws LookupException {
		initTargetLanguage();
		
		System.out.println("-- Complete rebuild");
		List<CompilationUnit> result = new ArrayList<CompilationUnit>();

		// First clone the compilation units to the target language.
		for (CompilationUnit cu : allProjectCompilationUnits) {
			CompilationUnit clone = implementationCompilationUnit(cu);
			result.add(clone);
		}
				
		for (CompilationUnit cu : result) {
			buildProgressHelper.checkForCancellation();
			_translator.weave(cu, result);
			buildProgressHelper.addWorked(1);
		}
		
		System.out.println("Rebuilt " + result.size() + " compilationUnit(s)");
		
		return result;
	}
}

