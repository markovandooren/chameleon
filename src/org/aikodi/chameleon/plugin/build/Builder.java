package org.aikodi.chameleon.plugin.build;

import java.io.File;
import java.util.List;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.plugin.ViewPlugin;

public interface Builder extends ViewPlugin {

	public void build(List<Document> compilationUnits, File outputDir, BuildProgressHelper buildProgressHelper) throws BuildException;
	public int totalAmountOfWork(List<Document> compilationUnits, List<Document> allProjectCompilationUnits);
}
