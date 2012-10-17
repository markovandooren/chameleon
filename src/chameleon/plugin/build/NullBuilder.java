package chameleon.plugin.build;

import java.io.File;
import java.io.IOException;
import java.util.List;

import chameleon.core.document.Document;
import chameleon.exception.ModelException;
import chameleon.plugin.ViewPluginImpl;

public class NullBuilder extends ViewPluginImpl implements Builder {
	
	public NullBuilder() {
		// Just here to be able to search where the default constructor is invoked.
	}
	
	@Override
	public void build(List<Document> compilationUnits, List<Document> allProjectCompilationUnits, File outputDir, BuildProgressHelper buildProgressHelper)
			throws ModelException, IOException {
	}

	@Override
	public int totalAmountOfWork(List<Document> compilationUnits, List<Document> allProjectCompilationUnits) {
		return 0;
	}

	@Override
	public NullBuilder clone() {
		return new NullBuilder();
	}
	
}

