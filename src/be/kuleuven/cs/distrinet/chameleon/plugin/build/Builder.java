package be.kuleuven.cs.distrinet.chameleon.plugin.build;

import java.io.File;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.plugin.ViewPlugin;

public interface Builder extends ViewPlugin {

	public void build(List<Document> compilationUnits, File outputDir, BuildProgressHelper buildProgressHelper) throws BuildException;
	public int totalAmountOfWork(List<Document> compilationUnits, List<Document> allProjectCompilationUnits);
}
