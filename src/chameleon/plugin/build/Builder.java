package chameleon.plugin.build;

import java.io.IOException;
import java.util.List;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.exception.ModelException;
import chameleon.plugin.Plugin;

public interface Builder extends Plugin {

	public void build(CompilationUnit compilationUnit, List<CompilationUnit> allProjectCompilationUnits) throws ModelException, IOException;
	
}
