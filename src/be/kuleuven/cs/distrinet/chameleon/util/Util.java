/*
 * @author Marko van Dooren
 * @author Nele Smeets
 * @author Kristof Mertens
 * @author Jan Dockx
 *
 */
package be.kuleuven.cs.distrinet.chameleon.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import be.kuleuven.cs.distrinet.rejuse.java.collections.Visitor;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;

/**
 * @author Marko van Dooren
 */
public class Util {
  public static String concat(List list) {
    if (list.size() > 0) {
      //    Object first = list.get(0);
      //    list.remove(0);
      final StringBuffer result = new StringBuffer();
      Iterator iter = list.iterator();
      while (iter.hasNext()) {
        result.append(iter.next().toString());
        if (iter.hasNext()) {
          result.append(".");
        }
      }
      //    new Visitor() {
      //      public void visit(Object o) {
      //        result.append("."+o.toString());
      //      }
      //    }
      //    .applyTo(list);

      return result.toString();
    } else {
      return "";
    }
  }

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
