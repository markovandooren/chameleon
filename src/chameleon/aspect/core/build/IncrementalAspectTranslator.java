package chameleon.aspect.core.build;

import java.util.ArrayList;
import java.util.List;

import chameleon.aspect.core.model.aspect.Aspect;
import chameleon.aspect.core.model.language.AspectOrientedLanguage;
import chameleon.aspect.core.weave.AspectWeaver;
import chameleon.aspect.core.weave.JoinPointWeaver;
import chameleon.core.compilationunit.Document;
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
	
	public List<Document> build(Document dummy, List<Document> allProjectCompilationUnits, BuildProgressHelper buildProgressHelper) throws LookupException {
		initTargetLanguage(true);
		
		System.out.println("-- Complete rebuild");
		List<Document> result = new ArrayList<Document>();

		// First clone the compilation units to the target language.
		for (Document cu : allProjectCompilationUnits) {
			Document clone = implementationCompilationUnit(cu);
			result.add(clone);
		}
		List<List<JoinPointWeaver>> heads = new ArrayList<List<JoinPointWeaver>>(); 	
		for (Document cu : result) {
			buildProgressHelper.checkForCancellation();
			heads.add(_translator.weave(cu, result));
			buildProgressHelper.addWorked(1);
		}
		for(List<JoinPointWeaver> w: heads) {
			for(JoinPointWeaver weaver: w) {
				weaver.weave();
			}
		}
		
		System.out.println("Rebuilt " + result.size() + " compilationUnit(s)");
		
		return result;
	}
}

