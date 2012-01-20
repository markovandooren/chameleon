package chameleon.aspect.core.build;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import chameleon.aspect.core.model.language.AspectOrientedLanguage;
import chameleon.aspect.core.weave.AspectWeaver;
import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.language.Language;
import chameleon.exception.ModelException;
import chameleon.plugin.Plugin;
import chameleon.plugin.PluginImpl;
import chameleon.plugin.build.BuildProgressHelper;
import chameleon.plugin.build.Builder;
import chameleon.plugin.build.CompilationUnitWriter;

public class AspectBuilder extends PluginImpl implements Builder {
	public AspectBuilder(AspectOrientedLanguage source, Language target, AspectWeaver weaver) {
		this(weaver);
		// Set target first, since it is needed in setLanguage()
		_target = target;
		setLanguage(source, Builder.class);
		//FIXME we should clone this on a clone of this builder. Create baseClone() in Language?
	}
	
	public AspectBuilder(AspectWeaver weaver) {
		_weaver = weaver;
}

	// We have to keep a redundant reference here because we cannot otherwise pass it to the incremental translator.
	private Language _target;
	
	@Override
	public <T extends Plugin> void setLanguage(Language lang, Class<T> pluginInterface) {
		super.setLanguage(lang, pluginInterface);
		Language target = _target;
		_translator = new IncrementalAspectTranslator((AspectOrientedLanguage) lang, target, weaver());
	}

	public AspectWeaver weaver() {
		return _weaver;
	}
	
	private AspectWeaver _weaver;

	public Language targetLanguage() {
		return _translator.targetLanguage();
	}

	public Language sourceLanguage() {
		return _translator.sourceLanguage();
	}

	private IncrementalAspectTranslator _translator;

	@Override
	public Plugin clone() {
		return new AspectBuilder(weaver());
	}

	@Override
	public void build(List<CompilationUnit> compilationUnits, List<CompilationUnit> allProjectCompilationUnits,	BuildProgressHelper buildProgressHelper) throws ModelException, IOException {
			Collection<CompilationUnit> cus = _translator.build(null,allProjectCompilationUnits, buildProgressHelper);
			CompilationUnitWriter writer = targetLanguage().plugin(CompilationUnitWriter.class);
			for (CompilationUnit translated : cus) {
				writer.write(translated);
			}
	}

	@Override
	public int totalAmountOfWork(List<CompilationUnit> compilationUnits, List<CompilationUnit> allProjectCompilationUnits) {
		return allProjectCompilationUnits.size();
	}
}