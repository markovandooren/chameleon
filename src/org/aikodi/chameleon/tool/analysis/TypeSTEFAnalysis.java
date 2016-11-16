package org.aikodi.chameleon.tool.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.rejuse.java.collections.IntegerAccumulator;
import org.aikodi.rejuse.java.collections.Visitor;
import org.aikodi.rejuse.predicate.SafePredicate;

/**
 * @author Marko van Dooren
 */
public class TypeSTEFAnalysis {
  
  public TypeSTEFAnalysis() {
    _analyses = new ArrayList();
  }
  
 /*@
   @ public behavior
   @
   @ pre analysis != null;
   @ pre ! getMethodAnalyses().contains(analysis);
   @
   @ post getMethodAnalyses().contains(analysis);
   @*/
  public void add(MethodSTEFAnalysis analysis) {
    if(! analysis.getExceptions().isEmpty()) {
      _analyses.add(analysis);
    }
  }

 /*@
   @ public behavior
   @
   @ post \result 
   @*/
  public List getMethodAnalyses() {
    return new ArrayList(_analyses);
  }
  
 /*@
   @ public behavior
   @
   @ post \result == getMethodAnalyses().size();
   @*/
  public int getNbAnalyses() {
    return _analyses.size();
  }
  
  public List getExceptions() {
    final List result = new ArrayList();
    new Visitor() {
      @Override
      public void visit(Object element) {
        result.addAll(((MethodSTEFAnalysis)element).getExceptions());
      }
    }.applyTo(_analyses);
    return result;
  }
  
  public Set getHeaderExceptions() {
    final Set result = new HashSet();
    new Visitor() {
      @Override
      public void visit(Object element) {
        result.addAll(((MethodSTEFAnalysis)element).getHeaderExceptions());
      }
    }.applyTo(_analyses);
    return result;
  }
  
  public Set getThrownExceptions() {
    final Set result = new HashSet();
    new Visitor() {
      @Override
      public void visit(Object element) {
        result.addAll(((MethodSTEFAnalysis)element).getThrownExceptions());
      }
    }.applyTo(_analyses);
    return result;
  }
  
  public int getNbHeaderPropagations(final Type type) {
    return new IntegerAccumulator() {
      @Override
      public int initialAccumulator() {
        return 0;
      }
      @Override
      public int accumulate(Object element, int acc) {
        ExceptionFlow flow = ((MethodSTEFAnalysis)element).getHeaderAnalysis(type); 
        return acc + (flow != null ? flow.getGraph().nbNodes() -1 : 0);
      }
    }.in(getMethodAnalyses());
  }
  
  public int getNbHeaders(final Type type) {
    return new SafePredicate<MethodSTEFAnalysis>() {
      @Override
      public boolean eval(MethodSTEFAnalysis o) {
        return o.getHeaderAnalysis(type) != null;
      }
    }.count(_analyses);
  }
  
  public int getNbThrowPropagations(final Type type) {
    return new IntegerAccumulator() {
      @Override
      public int initialAccumulator() {
        return 0;
      }
      @Override
      public int accumulate(Object element, int acc) {
        ExceptionFlow flow = ((MethodSTEFAnalysis)element).getThrowAnalysis(type); 
        return acc + (flow != null ? flow.getGraph().nbNodes() -1 : 0);
      }
    }.in(getMethodAnalyses());
  }
  
  public int getNbThrows(final Type type) {
    return new SafePredicate<MethodSTEFAnalysis>() {
      @Override
      public boolean eval(MethodSTEFAnalysis o) {
        return o.getThrowAnalysis(type) != null;
      }
    }.count(_analyses);
  }
  
  public Set getThrowingMethods() {
    Set result = new HashSet(getMethodAnalyses());
    new SafePredicate<MethodSTEFAnalysis>() {
      @Override
      public boolean eval(MethodSTEFAnalysis o) {
        return o.hasDirectThrow();
      }
    }.filter(result);
    return result;
  }
  
  public Set getPropagatingMethods() {
    final Set propagating = new HashSet();
    new Visitor() {
      @Override
      public void visit(Object element) {
        MethodSTEFAnalysis analysis =  (MethodSTEFAnalysis)element;
        new Visitor() {
          @Override
         public void visit(Object element2) {
            ExceptionFlow flow = (ExceptionFlow)element2;
            boolean remove = true;
            if(propagating.contains(flow.getMethod())){
              remove = false;
            }
            propagating.addAll(flow.getGraph().objects());
            if(remove) {
              //System.out.println("Removing "+flow.getMethod().getNearestType().getFullyQualifiedName()+":"+flow.getMethod().getName());
              propagating.remove(flow.getMethod());
            }
          }
        }.applyTo(analysis.getAnalyses());
      }
    }.applyTo(getMethodAnalyses());
    return propagating;
  }
  
  private List _analyses;
}
