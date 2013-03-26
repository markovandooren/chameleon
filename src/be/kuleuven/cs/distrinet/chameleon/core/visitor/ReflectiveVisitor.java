package be.kuleuven.cs.distrinet.chameleon.core.visitor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;

/**
 * ReflectiveVisitor is a visitor based on Java reflection for 
 * visiting a tree of Chameleon Elements.
 * 
 * To use this visitor, you have to do three things:
 * 
 * 1) Make ONE interface per language with visit methods for each element that is used by that language. This
 *    interface should be provided by the module for that language.
 *    For each type A (A extends from Element) with the signature visit(A param).
 * 
 * 2) Make a new class that inherits from ReflectiveVisitor or one of its subclasses, and that implements the
 *    interface that was made for that language in step (1).
 * 
 * 3) Define a method for each type A (A extends from Element) with the signature visit(A param)
 * 		--> If a method is lacking for one of the types, the defaultVisit method
 * 			will be used that by default throws an exception called NoVisitorForTypeException.
 * 		--> The defaultVisit method can be overridden by a subclass, so you can define your 
 * 			own default behavior.
 *  
 * 		e.g. for the type NamespacePart you define a method
 * 	
 * 		visit(NamespacePart s){
 * 			System.out.println("This is a NamespacePart");
 * 		} 
 * 
 * 
 * 4) Decide if you want to use automatic recursive version, or manual recursion.
 *	 	
 *		visit(...) is used when you want the visitor to automatically visit the children
 *		
 *		nonRecursiveVisit(...) is used when you want to choose yourselve when to visit the children (manual recursion), 
 *			in that case each method define in step three has to contain a call visitChildren(...).
 *
 *		e.g. for using manual recursion with the visit method the previous example can be altered as:
 * 
 * 		nonRecursiveVisit(NamespacePart s){
 * 			System.out.println("namespace " + s.namespace().name()+" {");
 * 			visitChildren(s);
 * 			System.out.println("}");
 * 		} 	
 * 
 * @author Nelis Boucke
 */
public abstract class ReflectiveVisitor {

	/**
	 * Recursively visits the elements. For the type A of element this method first calls visitA(...) and then
	 * recursively visits the children of element.
	 * @param element
	 * @throws NoVisitorForTypeException When no appropriate visit method for this type of element is defined.
	 */
	public void recursiveVisit(Element element) throws NoVisitorForTypeException{
		nonRecursiveVisit(element);
		resursiveVisitChildren(element);
	}
	
	/**
	 * Non-recursively visits the element. When using this method you are supposed to call 
	 * the visitChildren(...) manually.
	 * 
	 * @param element
	 * @throws NoVisitorForTypeException When no appropriate visit method for this type of element is defined.
	 */
	public void nonRecursiveVisit(Element element) throws NoVisitorForTypeException{
		String typename = element.getClass().getSimpleName();
		
		Class<? extends ReflectiveVisitor>  currentClass = this.getClass();
		
		try {
			Method method =
				currentClass.getMethod("visit", new Class[]{element.getClass()});
			method.invoke(this, element);
		} catch (SecurityException exc) {
			this.defaultVisit(element, exc);
		} catch (NoSuchMethodException exc) {
			this.defaultVisit(element,exc);
		} catch (IllegalArgumentException exc) {
			this.defaultVisit(element,exc);
		} catch (IllegalAccessException exc) {
			this.defaultVisit(element,exc);
		} catch (InvocationTargetException exc) {
			this.defaultVisit(element,exc);
		}
	}
	
	/**
	 * A method that (non-recursively) visits the children of this element.
	 * @param element
	 * @throws NoVisitorForTypeException When no appropriate visit method for one of the types of children of element is defined.
	 */
	protected void visitChildren(Element element) throws NoVisitorForTypeException{
		List<? extends Element> children = element.children();
		for (Element child : children) {
			nonRecursiveVisit(child);
		}
	}
	
	private void resursiveVisitChildren(Element e) throws NoVisitorForTypeException{
		List<? extends Element> children = e.children();
		for (Element child : children) {
			recursiveVisit(child);
		}
	}
	
	/**
	 * The method called if no appropriate visit method is defined for the type of element.
	 * The default behavior of this method is to throw the NoVisitorForTypeException, override
	 * this method to change the default behavior.
	 * 
	 * @param element
	 * @param cause The exception indicating that no appropriate visit method is defined 
	 * 	for this type of element. This exception comes from a reflective call and is only intended
	 *  for debugging purposes.
	 * @throws NoVisitorForTypeException Thrown when no default behavior is defined for this type.
	 */
	public void defaultVisit(Element element, Exception cause) throws NoVisitorForTypeException{
		 throw new NoVisitorForTypeException(element,cause);
	}
	
}
