package be.kuleuven.cs.distrinet.chameleon.aspect.core.build;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.language.AspectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.weave.AspectWeaver;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.weave.JoinPointWeaver;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.plugin.build.BuildException;
import be.kuleuven.cs.distrinet.chameleon.plugin.build.BuildProgressHelper;
import be.kuleuven.cs.distrinet.chameleon.support.translate.IncrementalTranslator;
import be.kuleuven.cs.distrinet.chameleon.util.Lists;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputException;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;

public class IncrementalAspectTranslator extends IncrementalTranslator<AspectOrientedLanguage, Language> {

	public IncrementalAspectTranslator(View source, View target, AspectWeaver weaver) {
		super(source, target);
		_translator = weaver;
	}
	
	private AspectWeaver _translator;
	
	public AspectWeaver basicTranslator() {
		return _translator;
	}
	
	public List<Document> build(Document dummy, BuildProgressHelper buildProgressHelper) throws BuildException {
		try {
		initTargetLanguage(true);
		
		System.out.println("-- Complete rebuild");
		List<Document> result = Lists.create();

		// First clone the compilation units to the target language.
		for (Document cu : source().sourceDocuments()) {
			Document clone = implementationCompilationUnit(cu);
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

