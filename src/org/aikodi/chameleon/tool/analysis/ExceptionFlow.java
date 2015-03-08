package org.aikodi.chameleon.tool.analysis;

import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.type.Type;

import be.kuleuven.cs.distrinet.rejuse.graph.Graph;

/**
 * A class of objects representating the static flow of exceptions through a program.
 */
public class ExceptionFlow {

 /*@
   @ public behavior
   @
   @ post getType() == type;
   @*/
  public ExceptionFlow(Method method, Type type) {
    _graph = new Graph();
    _type = type;
    _method = method;
  }
  
  /**
   * Return the source method of this exception flow.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/ 
  public Method getMethod() {
    return _method;
  }
  
  private Method _method;

 /*@
   @ post \result != null;
   @*/
  public Graph getGraph() {
    return _graph;
  }
  
  private Graph _graph;
  
  /**
   * Return the exception type of which this is the exception flow. 
   */
  public Type getType() {
    return _type;
  }
  
  private Type _type;
  
}
