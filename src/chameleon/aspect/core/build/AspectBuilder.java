package chameleon.aspect.core.build;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import chameleon.aspect.core.model.language.AspectOrientedLanguage;
import chameleon.aspect.core.weave.AspectWeaver;
import chameleon.core.document.Document;
import chameleon.core.language.Language;
import chameleon.exception.ModelException;
import chameleon.plugin.ViewPlugin;
import chameleon.plugin.ViewPluginImpl;
import chameleon.plugin.build.BuildProgressHelper;
import chameleon.plugin.build.Builder;
import chameleon.plugin.build.CompilationUnitWriter;
import chameleon.workspace.View;

public class AspectBuilder extends ViewPluginImpl implements Builder {
	public AspectBuilder(View source, View target, AspectWeaver weaver) {
		this(weaver);
		// Set target first, since it is needed in setLanguage()
		_target = target;
		setContainer(source, Builder.class);
		//FIXME we should clone this on a clone of this builder. Create baseClone() in Language?
	}
	
	public AspectBuilder(AspectWeaver weaver) {
		_weaver = weaver;
}

	// We have to keep a redundant reference here because we cannot otherwise pass it to the incremental translator.
	private View _target;
	
	@Override
	public <T extends ViewPlugin> void setContainer(View view, Class<T> pluginInterface) {
		super.setContainer(view, pluginInterface);
		View target = _target;
//		FIXME: next line commented out
		_translator = new IncrementalAspectTranslator(view, target, weaver());
	}

	public AspectWeaver weaver() {
		return _weaver;
	}
	
	private AspectWeaver _weaver;

	public View target() {
		return _translator.target();
	}

	public View source() {
		return _translator.source();
	}

	private IncrementalAspectTranslator _translator;

	@Override
	public AspectBuilder clone() {
		return new AspectBuilder(weaver());
	}

	@Override
	public void build(List<Document> compilationUnits, List<Document> allProjectCompilationUnits,	File outputDir, BuildProgressHelper buildProgressHelper) throws ModelException, IOException {
			Collection<Document> cus = _translator.build(null,allProjectCompilationUnits, buildProgressHelper);
			CompilationUnitWriter writer = target().language().plugin(CompilationUnitWriter.class);
			for (Document translated : cus) {
				writer.write(translated,outputDir);
			}
	}

	@Override
	public int totalAmountOfWork(List<Document> compilationUnits, List<Document> allProjectCompilationUnits) {
		return allProjectCompilationUnits.size();
	}
}