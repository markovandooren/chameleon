package org.aikodi.chameleon.tool.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.oo.type.Type;

import be.kuleuven.cs.distrinet.rejuse.java.collections.Visitor;

/**
 * @author Marko van Dooren
 */
public class MethodSTEFAnalysis {
  
 /*@
   @ public behavior
   @
   @ post getSignatureAnalyses().isEmpty();
   @ post getThrowAnalyses().isEmpty();
   @*/
  public MethodSTEFAnalysis() {
  }

 /*@
   @ public behavior
   @
   @ post (\forall Object o;;
   @         \result.contains(o) <==> getThrowAnalyses().contains(o) ||
   @                                  getHeaderAnalyses().contains(o));
   @*/
  public Collection getAnalyses() {
    Collection result = getThrowAnalyses();
    result.addAll(getHeaderAnalyses());
    return result;
  }
  
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public List getThrowAnalyses() {
    return new ArrayList(_throwAnalysis.values());
  }
 
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public List getHeaderAnalyses() {
    return new ArrayList(_headerAnalysis.values());
  }
 
 /*@
   @ public behavior
   @
   @ pre flow != null;
   @
   @ post getHeaderAnalyses().contains(flow);
   @*/
  public void addHeaderAnalysis(ExceptionFlow flow) {
    _headerAnalysis.put(flow.getType(), flow);
  }

 /*@
   @ public behavior
   @
   @ pre flow != null;
   @
   @ post getThrowAnalyses().contains(flow);
   @*/
  public void addThrowAnalysis(ExceptionFlow flow) {
    _throwAnalysis.put(flow.getType(), flow);
  }
  
 /*@
   @ public behavior
   @
   @ pre type != null;
   @
   @ post getSignatureAnalyses().contains(\result);
   @ post \result.getType() == type;
   @*/
  public ExceptionFlow getHeaderAnalysis(Type type) {
    return (ExceptionFlow)_headerAnalysis.get(type);
  }

 /*@
   @ public behavior
   @
   @ pre type != null;
   @
   @ post getThrowAnalyses().contains(\result);
   @ post \result.getType() == type;
   @*/
  public ExceptionFlow getThrowAnalysis(Type type) {
    return (ExceptionFlow)_throwAnalysis.get(type);
  }
  
  public List getExceptions() {
    final List result = new ArrayList();
    new Visitor() {
      @Override
      public void visit(Object element) {
        result.add(((ExceptionFlow)element).getType());
      }
    }.applyTo(getAnalyses());
    return result;
  }

  public Set getThrownExceptions() {
    final Set result = new HashSet();
    new Visitor() {
      @Override
      public void visit(Object element) {
        result.add(((ExceptionFlow)element).getType());
      }
    }.applyTo(getThrowAnalyses());
    return result;
  }

 /*@
   @ public behavior
   @
   @ post \result == getThrownExceptions().size() > 0;
   @*/
  public boolean hasDirectThrow() {
    return getThrownExceptions().size() > 0;
  }
  
  public Set getHeaderExceptions() {
    final Set result = new HashSet();
    new Visitor() {
      @Override
      public void visit(Object element) {
        result.add(((ExceptionFlow)element).getType());
      }
    }.applyTo(getHeaderAnalyses());
    return result;
  }

  private HashMap _headerAnalysis = new HashMap();
  
  private HashMap _throwAnalysis = new HashMap();
  
}
