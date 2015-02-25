/*
 * @author Marko van Dooren
 * @author Nele Smeets
 * @author Kristof Mertens
 * @author Jan Dockx
 *
 */
package org.aikodi.chameleon.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.oo.expression.Expression;

import be.kuleuven.cs.distrinet.rejuse.java.collections.Visitor;

/**
 * @author Marko van Dooren
 */
public class Util {
	
	public static <T extends Element> T clone(T element) {
		T result = (T) element.clone();
//		if(! element.getClass().isInstance(result)) {
//			throw new CloneException("The clone of a "+element.getClass().getName()+ " is a "+result.getClass().getName());
//		}
//		if(Config.debug()) {
//		List<? extends Element> originalChildren = element.children();
//		int originalSize = originalChildren.size();
//		List<? extends Element> cloneChildren = result.children();
//		int resultSize = cloneChildren.size();
//		if(originalSize != resultSize) {
//			throw new ChameleonProgrammerException("The clone of an element of type "+element.getClass().getName()+ " with "+originalSize+ " chilren has "+resultSize+" children.");
//		}
		//		}
		return result;
	}
	
	/**
	 * Just a method to set a breakpoint during debugging.
	 * Set the breakpoint on the statement inside the if statement.
	 * The breakpoint will then be hit when the given value is true.
	 */
	public static void debug(boolean halt) {
		if(halt) {
			System.out.println("debug");
		}
	}
	
  public static String concat(List list) {
    if (list.size() > 0) {
      final StringBuilder result = new StringBuilder();
      Iterator iter = list.iterator();
      while (iter.hasNext()) {
        result.append(iter.next().toString());
        if (iter.hasNext()) {
          result.append(".");
        }
      }
      return result.toString();
    } else {
      return "";
    }
  }

  /**
   * Clone a list of elements.
   * @return
   */
  public static <T extends Element> List<T> clone(List<T> original) {
  	List<T> result = new ArrayList<T>();
  	for(T t: original) {
  		result.add((T)t.clone());
  	}
  	return result;
  }
  
  /**
    * @param indent
    * @param tab
    * @param string
    * @return
    */
  /*@
    @ public behavior
    @*/
  public static String indent(int indent, int tab, String string) {
    StringBuffer result = new StringBuffer();
    for(int i = 0; i< indent*tab; i++) {
      result.append(' ');
    }
    result.append(string);
    return result.toString();
  }

  public static String getFirstPart(String string) {
    if(string == null) {
      return null;
    }
    int dot = string.indexOf(".");
    if(dot > 0) {
      return string.substring(0,dot);
    }
    else {
      return string;
    }
  }

  public static String getAllButFirstPart(String string) {
    if(string == null) {
      return null;
    }
    int dot = string.indexOf(".");
    if(dot > 0) {
      return string.substring(dot+1,string.length());
    }
    else {
      return null;
    }
  }

  /**
   * Returns everything before the last '.' character.
   */
  public static String getAllButLastPart(String string) {
    if(string == null) {
      return null;
    }
    int dot = string.lastIndexOf(".");
    if(dot > 0) {
      return string.substring(0,dot);
    }
    else {
      return null;
    }
  }



  public static Set createSingletonSet(Object element) {
    Set result = new HashSet();
    result.add(element);
    return result;
  }

  public static <T> List<T> createSingletonList(T element) {
    List<T> result = new ArrayList<T>();
    result.add(element);
    return result;
  }

  public static Set createExpressionSet(Expression element) {
    Set result = new HashSet();
    addExpression(element, result);
    return result;
  }

  public static Set createTopExpressionSet(Expression element) {
    Set result = new HashSet();
    addTopExpression(element, result);
    return result;
  }

  public static void addTopExpression(Expression expression, Set set) {
    if(expression != null) {
      set.add(expression);
    }
  }

	public static void addExpression(Expression expression, Set set) {
    if(expression != null) {
      set.add(expression);
        set.addAll(expression.descendants(Expression.class));
      //set.addAll(expression.getAllExpressions());
    }
	}

	/**
	 * @param list
	 * @param result
	 */
	public static void addExpressions(List list, final Set result) {
    new Visitor() {
		    @Override
         public void visit(Object element) {
          addExpression((Expression)element, result);
		    }
		}.applyTo(list);
	}

	/**
	 * @param string
	 * @return
	 */
	public static String getLastPart(String string) {
    if(string == null) {
      return null;
    }
    int dot = string.lastIndexOf(".");
    if(dot > 0) {
      return string.substring(dot+1,string.length());
    }
    else {
      return string;
    }
  }

	/**
	 * @param string
	 * @return
	 */
	public static String getPartAfterFirstDot(String string) {
    if(string == null) {
      return null;
    }
    int dot = string.indexOf(".");
    if(dot > 0) {
      return string.substring(dot+1,string.length());
    }
    else {
      return null;
    }
  }

  /**
   * @param target
   * @param result
   */
  public static void addNonNull(Object element, Collection result) {
    if(element != null) {
      result.add(element);
    }
  }

  public static Set createNonNullSet(Object object) {
    Set result = new HashSet();
    if(object != null) {
      result.add(object);
    }
    return result;
  }

  public static List createNonNullList(Object object) {
    List result = new ArrayList(1);
    if(object != null) {
      result.add(object);
    }
    return result;
  }
  
  /**
   * Checks whether the array contains the given element
   */
  public static boolean arrayContains(Object[] array, Object element){
          for(Object currElement : array){
                  if(currElement.equals(element)){
                          return true;
                  }
          }
          return false;
  }

  public static String repeatString(String string, int nbTimes){
  	String result = "";
  	for(int i=0; i<nbTimes; i++){
  		result += string;
  	}
  	return result;
  }

public static <T extends Element> T cloneOrNull(T element) {
	if (element == null)
		return null;
	
	return (T) element.clone();
}


}
