package chameleon.linkage;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.element.Element;

/**
 * Interface supporting the linking of chameleon
 * with other applications.
 * 
 * note: at the moment this is specifically aimed at the
 * chameleon editor, but in the future this may be broadened
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
	 * Define the given position to have a relationship with the element el
	 * defined by the string dectype
	 * @param offset offset of the position
	 * @param length length of the position
	 * @param dectype type of the relationship
	 * @param el element of the relationship
	 */
	void decoratePosition(int offset, int length, String dectype, Element el);

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
