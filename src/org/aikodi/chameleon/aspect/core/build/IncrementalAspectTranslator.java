package org.aikodi.chameleon.aspect.core.build;

import org.aikodi.chameleon.aspect.core.model.language.AspectOrientedLanguage;
import org.aikodi.chameleon.aspect.core.weave.AspectWeaver;
import org.aikodi.chameleon.aspect.core.weave.JoinPointWeaver;
import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.plugin.build.BuildException;
import org.aikodi.chameleon.plugin.build.BuildProgressHelper;
import org.aikodi.chameleon.support.translate.IncrementalTranslator;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.workspace.InputException;
import org.aikodi.chameleon.workspace.View;

import java.util.List;

public class IncrementalAspectTranslator extends IncrementalTranslator<AspectOrientedLanguage, Language> {

	public IncrementalAspectTranslator(View source, View target, AspectWeaver weaver) {
		super(source, target);
		_translator = weaver;
	}
	
	private AspectWeaver _translator;
	
	public AspectWeaver basicTranslator() {
		return _translator;
	}
	
	@Override
   public List<Document> build(Document dummy, BuildProgressHelper buildProgressHelper) throws BuildException {
		try {
		initTarget(true);
		
		System.out.println("-- Complete rebuild");
		List<Document> result = Lists.create();

		// First clone the compilation units to the target language.
		for (Document cu : source().sourceDocuments()) {
			Document clone = targetDocument(cu);
			result.add(clone);
		}
		List<List<JoinPointWeaver>> heads = Lists.create(); 	
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
		 // trying to maintain Java 1.5 compatibility for a while to support 32-bit VMs.
		} catch(LookupException exc) {
			throw new BuildException(exc);
		} catch(InputException exc) {
			throw new BuildException(exc);
		}
	}
}

