package chameleon.tool.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.rejuse.graph.Graph;
import org.rejuse.graph.Node;
import org.rejuse.graph.UniEdgeFactory;

import chameleon.core.expression.Expression;
import chameleon.core.expression.VariableReference;
import chameleon.core.member.Member;
import chameleon.core.method.Method;
import chameleon.core.method.exception.AnchoredExceptionDeclaration;
import chameleon.core.method.exception.ExceptionDeclaration;
import chameleon.core.method.exception.TypeExceptionDeclaration;
import chameleon.core.statement.Block;
import chameleon.core.statement.ExceptionSource;
import chameleon.core.variable.FormalParameter;
import chameleon.core.variable.Variable;
import chameleon.oo.type.Type;
import chameleon.support.statement.CatchClause;
import chameleon.support.statement.ThrowStatement;

/**
 * @author marko
 */
@SuppressWarnings("unchecked")
public class ExceptionFlowAnalyzer {
  
 /*@
   @ public behavior
   @
   @ post getAllMethods().equals(allMethods);
   @*/
  public ExceptionFlowAnalyzer(List allMethods) throws MetamodelException {
    _allMethods = new ArrayList(allMethods);
    _callGraph = new Graph(new UniEdgeFactory());
    computeCallGraph();
  }
  
  public boolean getCache() {
    return _cache;
  }
  
  public void setCache(boolean cache) {
    _cache = cache;
  }
  
  private boolean _cache = false;
  
  private HashMap _clientMap = new HashMap();
  
  private List _allMethods;
  
  private Graph _callGraph;
  
  private Graph getCallGraph() {
    return _callGraph;
  }
  
  private void computeCallGraph() throws MetamodelException {
    System.out.println("Constructing call graph");
    Iterator iter = _allMethods.iterator();
    while(iter.hasNext()) {
      Method method = (Method)iter.next();
      System.out.print(".");
      Block block = method.getBody();
      if (block != null) {
        // Add the method to the call graph
        getCallGraph().addNode(method);
        // Retrieve all method invocations
        List descendants = block.descendants(Invocation.class);
        
        Iterator descIter = descendants.iterator();
        while (descIter.hasNext()) {
          Invocation inv = (Invocation)descIter.next();
          Method invoked = inv.getMethod(); 
          getCallGraph().addNode(invoked);
          // Register 'method' as a client of 'invoked'
          getCallGraph().addEdge(invoked, method);
        }
      }
    }
    System.out.println("Done");
  }
  
  private Set getDirectClients(Method method) {
    Node node = getCallGraph().getNode(method);
    if(node != null) {
      return node.getDirectlyConnectedObjects();
    } else {
      return new HashSet();
    }
  }
  
  
  /**
   * Return all methods that will be analyzed when computing the flow of exceptions.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public List getAllMethods() {
    return new ArrayList(_allMethods);
  }
  
  //private HashMap _invocationMap = new HashMap();

public List getClients(final Method method) throws MetamodelException {
    List allMethods = null;
    if (_cache) {
      allMethods = (List)_clientMap.get(method);
    }

    if (allMethods == null) {
      Set temp = getDirectClients(method);
      Collection<Member> members = method.directlyOverriddenMembers();
      for(Member member:members) {
        Method superMethod = (Method)member;
        temp.addAll(getClients(superMethod));
      }
      allMethods = new ArrayList(temp);
      if (_cache) {
        _clientMap.put(method, allMethods);
      }
    }
    //System.out.println("done computing clients");
    return allMethods;
  }

  /**
   * Check whether or not 'method' propagates 'exception' when thrown by 'source'.
   * 
   * @param method
   *        The method to be inspected
   * @param source
   *        The method hypothetically thrown the exception
   * @param exception
   *        The type of the exception that is being thrown (hypothetically).
   */
 /*@
   @ public behavior
   @
   @ pre method != null;
   @ pre source != null;
   @ pre exception != null;
   @*/
  public boolean propagates(Method method, Method source, Type exception) throws MetamodelException {
      Block body = method.getBody();
      if (body != null) {
        List pairs = (List)_celMap.get(body);
        if(pairs == null) {
          pairs = body.getCEL().getPairs();
          _celMap.put(body, pairs);
        }
        Iterator iter = pairs.iterator();
        while (iter.hasNext()) {
          ExceptionPair pair = (ExceptionPair)iter.next();
          ExceptionDeclaration ed = pair.getDeclaration();
          if (ed instanceof AnchoredExceptionDeclaration) {
            AnchoredExceptionDeclaration ad = (AnchoredExceptionDeclaration)ed;
            if ((ad.getInvocation().getMethod() == source) && exception.assignableTo(pair.getException())) {
              return true;
            }
          }
        }
      }
    return false;
  }
  
  private HashMap _propagatesMap = new HashMap();
  private HashMap _celMap = new HashMap();
  
  public List analyzeMethods(List methods) throws MetamodelException {
    List result = new ArrayList();
    Iterator iter = methods.iterator();
    while(iter.hasNext()) {
      result.add(analyze((Method)iter.next()));
    }
    return result;
  }
  
  public List analyzeTypes(List types) throws MetamodelException {
    List result = new ArrayList();
    Iterator iter = types.iterator();
    while(iter.hasNext()) {
      result.add(analyze((Type)iter.next()));
    }
    return result;
  }
  
  
  public TypeSTEFAnalysis analyze(Type type) throws MetamodelException {
    TypeSTEFAnalysis result = new TypeSTEFAnalysis();
    Set<Method> methods = type.declaredMembers(Method.class);
    Iterator<Method> iter = methods.iterator();
    while(iter.hasNext()) {
      result.add(analyze(iter.next()));
    }
    return result;
  }
  
  public MethodSTEFAnalysis analyze(Method method) throws MetamodelException {
    MethodSTEFAnalysis result = new MethodSTEFAnalysis();
    //System.out.println("#### "+method.getNearestType().getFullyQualifiedName()+":"+method.getName());
    // Throw analysis
    Set excs = getDirectlyThrownExceptions(method);
    Iterator iter = excs.iterator();
    while(iter.hasNext()) {
      Type exception = (Type)iter.next(); 
      ExceptionFlow flow = new ExceptionFlow(method, exception);
      //System.out.println("Adding method "+method.getName()+" to graph.");
      analyze(method, flow, new HashSet());
      result.addThrowAnalysis(flow);
    }
    
    // Signature analysis
    excs = method.getWorstCaseExceptions();
    iter = excs.iterator();
    while(iter.hasNext()) {
      Type exception = (Type)iter.next(); 
      ExceptionFlow flow = new ExceptionFlow(method, exception);
      //System.out.println("Adding method "+method.getName()+" to graph.");
      analyze(method, flow, new HashSet());
      result.addHeaderAnalysis(flow);
    }
    return result;
  }
  
 /**
   * @param method
   * @return
   */
  private Set getDirectlyThrownExceptions(Method method) throws MetamodelException {
    Block block = method.getBody();
    Set result = new HashSet();
    if (block != null) {
      List pairs = block.getCEL().getPairs();
      Iterator iter = pairs.iterator();
      while (iter.hasNext()) {
        ExceptionPair pair = (ExceptionPair)iter.next();
        if ((pair.getDeclaration() instanceof TypeExceptionDeclaration)) {
          result.add(pair.getException());
        }
      }
    }
    return result;
  }

  /**
 * @param pair
 * @return
 */
private boolean propagatedThrows(ExceptionPair pair) throws MetamodelException {
  ExceptionSource cause = pair.getCause(); 
  if (! (cause instanceof ThrowStatement)) {
    return false;
  }
  ThrowStatement t = (ThrowStatement)cause;
  Expression expr = t.getExpression();
  if (expr instanceof VariableReference) {
    Variable v = ((VariableReference)expr).getVariable();
    if((v instanceof FormalParameter) &&(v.parent() instanceof CatchClause)) {
      return true;
    }
  }
  return false;
  
         
}

  /*@
   @ pre ! done.contains(method);
   @*/
  public void analyze(Method source, ExceptionFlow flow, Set done) throws MetamodelException {
    flow.getGraph().addNode(source);
    if (!done.contains(source)) {
      List invokers = getClients(source);
      done.add(source);
      Iterator iter = invokers.iterator();
      while (iter.hasNext()) {
        Method invokingMethod = (Method)iter.next();
        analyze(invokingMethod, source, flow, done);
      }
//      Set supers = source.getSuperMethods();
//      Iterator superIter = supers.iterator();
//      while(superIter.hasNext()) {
//        Method superMethod = (Method)superIter.next();
//        analyze(superMethod, flow, done);
//      }
    }
  }
  
  public void analyze(Method method, Method source, ExceptionFlow flow, Set done) throws MetamodelException {
    if (propagates(method, source, flow.getType())) {
      //System.out.println("Adding method "+method.getName()+" to graph.");
      flow.getGraph().addNode(method);
      //System.out.println("Adding edge "+source.getName()+" -> "+method.getName()+" to graph.");
      flow.getGraph().addEdge(source, method);
      analyze(method, flow, done);
    } else {
      //System.out.println("Method "+method.getName()+" does not propagate exception "+flow.getType().getFullyQualifiedName()+ " coming from method "+source.getName());
    }
  }
  
}
