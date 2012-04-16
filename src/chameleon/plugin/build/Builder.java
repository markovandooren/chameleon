package chameleon.plugin.build;

import java.io.IOException;
import java.util.List;

import chameleon.core.document.Document;
import chameleon.exception.ModelException;
import chameleon.plugin.Plugin;

public interface Builder extends Plugin {

	public void build(List<Document> compilationUnits, List<Document> allProjectCompilationUnits, BuildProgressHelper buildProgressHelper) throws ModelException, IOException;
	public int totalAmountOfWork(List<Document> compilationUnits, List<Document> allProjectCompilationUnits);
}
