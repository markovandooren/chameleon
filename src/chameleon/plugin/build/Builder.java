package chameleon.plugin.build;

import java.io.IOException;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.exception.ModelException;
import chameleon.plugin.Plugin;

public interface Builder extends Plugin {

	public void build(CompilationUnit compilationUnit) throws ModelException, IOException;
	
}
