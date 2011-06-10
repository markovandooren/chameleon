package chameleon.plugin.build;

import java.io.IOException;
import java.util.List;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.exception.ModelException;
import chameleon.plugin.Plugin;

public interface Builder extends Plugin {

	public void build(List<CompilationUnit> compilationUnits, List<CompilationUnit> allProjectCompilationUnits, BuildProgressHelper buildProgressHelper) throws ModelException, IOException;
	public int totalAmountOfWork(List<CompilationUnit> compilationUnits, List<CompilationUnit> allProjectCompilationUnits);
}
