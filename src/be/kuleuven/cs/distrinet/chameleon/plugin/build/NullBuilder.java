package be.kuleuven.cs.distrinet.chameleon.plugin.build;

import java.io.File;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.plugin.ViewPluginImpl;

public class NullBuilder extends ViewPluginImpl implements Builder {
	
	public NullBuilder() {
		// Just here to be able to search where the default constructor is invoked.
	}
	
	@Override
	public void build(List<Document> compilationUnits, File outputDir, BuildProgressHelper buildProgressHelper) {
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

