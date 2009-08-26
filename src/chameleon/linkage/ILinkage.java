package chameleon.linkage;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.element.Element;

/**
 * Interface supporting the linking of chameleon
 * with other applications.
 * 
 * note: at the moment this is specifically aimed at the
 * Chameleon editor, but in the future this may be broadened
 */
public interface ILinkage {
  //FIXME make this a chain of command such that different tools can e.g. add their own decorators and
	//      share the model.
	
	
	IParseErrorHandler getParseErrorHandler();

	/**
	 * Get source from application
	 */
	String getSource();

	/**
	 * Calculate the Line-Offset of the given position using the current
	 * datasource for the code
	 */
	int getLineOffset(int i);

	/**
	 * Called when a compilationUnit element is created
	 */
	void addCompilationUnit(CompilationUnit cu);

}
