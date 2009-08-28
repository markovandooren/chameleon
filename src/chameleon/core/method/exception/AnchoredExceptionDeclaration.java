package chameleon.core.method.exception;
public class AnchoredExceptionDeclaration{}
//
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//
//import org.rejuse.association.Reference;
//import org.rejuse.predicate.PrimitivePredicate;
//import org.rejuse.predicate.TypePredicate;
//
//import chameleon.core.MetamodelException;
//import chameleon.core.context.Context;
//import chameleon.core.expression.Expression;
//import chameleon.core.expression.ExpressionContainer;
//import chameleon.core.expression.Invocation;
//import chameleon.core.expression.InvocationTarget;
//import chameleon.core.expression.NonConstructorInvocation;
//import chameleon.core.expression.StubExpressionContainer;
//import chameleon.core.method.Method;
//import chameleon.core.reference.ReferenceContainer;
//import chameleon.core.type.Type;
//import chameleon.core.variable.FormalParameter;
//import chameleon.util.Util;
//
///**
// * @author marko
// */
//public class AnchoredExceptionDeclaration 
//       extends ExceptionDeclaration<AnchoredExceptionDeclaration> 
//       implements AnchoredDeclaration<AnchoredExceptionDeclaration,ExceptionClause>, ExpressionContainer<AnchoredExceptionDeclaration,ExceptionClause>, ReferenceContainer<AnchoredExceptionDeclaration,ExceptionClause> {
//
//  public AnchoredExceptionDeclaration(Invocation invocation, FilterClause filterClause) {
//
//	setInvocation(invocation);
//    setFilterClause(filterClause);
//  }
//
//  public AnchoredExceptionDeclaration(Invocation expr) {
//    this(expr, null);
//  }
//
//  /**
//   * INVOCATION
//   */
//  
//  private Reference<AnchoredExceptionDeclaration,Invocation> _invocation = new Reference<AnchoredExceptionDeclaration,Invocation>(this);
//  
//  public void setInvocation(Invocation invocation) {
//    if(invocation == null) {
//      _invocation.connectTo(null);
//    }
//    else {
//      _invocation.connectTo(invocation.getParentLink());
//    }
//  }
//
//  /**
//   * Return the method expression of this anchored exception declaration
//   */
// /*@
//   @ public behavior
//   @
//   @ post \result != null;
//   @*/
//  public Invocation getInvocation() {
//    return _invocation.getOtherEnd();
//  }
//
//  /**
//   * Check whether or not this anchored exception declarations is compatible with the given exception clause.
//   */
//  public boolean compatibleWith(ExceptionClause other) throws MetamodelException {
//    return compatibleWith(getInvocation(), other, getFilterClause());
//  }
//
////  private Collection getPropagatedTypes() throws NotResolvedException {
////    if(getFilterClause() != null) {
////      return getFilterClause().getPropagatedTypes(getRawExceptionTypes());
////    }
////    else {
////      return getRawExceptionTypes();
////      //return new ArrayList();
////    }
////  }
//
//  public boolean compatibleWith(Invocation inv, ExceptionClause other, FilterClause filterClause) throws MetamodelException {
//    //List otherDecls = other.getDeclarations();
//    boolean result = isDirectlyCompatibleWith(inv, other, filterClause);
//    if (!result) {
//      // expand
//      result = isExpandedCompatibleWith(inv, other, filterClause);
//    }
//    return result;
//  }
//
//  public boolean isExpandedCompatibleWith(final Invocation inv, final ExceptionClause other, FilterClause filterClause) throws MetamodelException {
//    try {
//      boolean result;
//      result = expandedCompatibleTypeDecls(other, inv, filterClause);
//      if (result) {
//        result = expandedCompatibleAnchors(inv, other, filterClause);
//      }
//      return result;
//    }
//    catch (MetamodelException exc) {
//      throw exc;
//    }
//    catch (Exception exc) {
//      exc.printStackTrace();
//      throw new Error();
//    }
//  }
//
//
//  private boolean expandedCompatibleAnchors(final Invocation inv, final ExceptionClause other, final FilterClause filterClause) throws MetamodelException, Exception {
//    List anchorDecls = inv.getMethod().getExceptionClause().getDeclarations();
//    // Check anchored declarations.
//    new TypePredicate(AnchoredExceptionDeclaration.class).filter(anchorDecls);
//    return new PrimitivePredicate() {
//      public boolean eval(final Object element) throws MetamodelException {
//        AnchoredExceptionDeclaration ad = (AnchoredExceptionDeclaration)element;
//        FilterClause newFilter = filterClause;
//        if(ad.getFilterClause() != null) {
//          newFilter = ad.getFilterClause().and(filterClause);
//          newFilter.setParent(new StubAnchoredExceptionDeclaration(ad.getInvocation())); // XXX parent klopt niet, maar is hier niet relevant
//        }
//        Invocation original = ad.getInvocation();
//        Invocation nc = getClonedInvocation(inv, original);
//        return compatibleWith(nc, other, newFilter);
//      }
//
//    }.forAll(anchorDecls);
//  }
//
//  private boolean expandedCompatibleTypeDecls(final ExceptionClause other, Invocation invocation, FilterClause filterClause) throws Exception {
//    // TODO: move method to Invocation
//    List absDecls = invocation.getMethod().getExceptionClause().getDeclarations();
//    // Check absolute declarations. They don't have to be prefixed
//    new TypePredicate(TypeExceptionDeclaration.class).filter(absDecls);
//    filterClause.filterDeclarations(absDecls);
//    return new PrimitivePredicate() {
//      public boolean eval(Object o) throws MetamodelException {
//        return ((TypeExceptionDeclaration)o).compatibleWith(other);
//      }
//    }.forAll(absDecls);
//  }
//
//  private void removeNonPropagatedTypes(List typeDecls, final Collection propagatedTypes) throws Exception {
//    // Filter the list to remove non-propagated types.
//    new PrimitivePredicate() {
//      public boolean eval(Object o) throws MetamodelException, Exception {
//        final Type type = ((TypeExceptionDeclaration)o).getType();
//        return new PrimitivePredicate() {
//          public boolean eval(Object o2) throws MetamodelException {
//            return type.assignableTo((Type)o2);
//          }
//        }.exists(propagatedTypes);
//      }
//    }.filter(typeDecls);
//  }
//
//  private Invocation getClonedInvocation(final Invocation beta, Invocation alpha) throws MetamodelException {
//    Context contextAlpha = alpha.lexicalContext();
//    Context contextBeta = beta.lexicalContext();
//
//    // 1) Clone
//    Invocation clonedAlpha = (Invocation)alpha.clone();
//    // 2) Set context to original context, it is needed for performing the prefix and
//    //    the parameter substitution
//    StubExpressionContainer stub = new StubExpressionContainer(AnchoredExceptionDeclaration.this, clonedAlpha, contextAlpha);
//
//    List<Expression> alphaExprs = clonedAlpha.getDescendants(Expression.class);//clonedAlpha.getAllExpressions();
//    // add 'clonedAlpha' to the set
//    alphaExprs.add(clonedAlpha);
//    Iterator alphaExprIterator = alphaExprs.iterator();
//
//
//    // 3) Lock signatures of method invocations in clonedAlpha
//    while(alphaExprIterator.hasNext()) {
//      Expression expr = (Expression)alphaExprIterator.next();
//      if(expr instanceof Invocation) {
//        Invocation invocation = (Invocation)expr;
//        expr.setSpecificContext(new SignatureBindingContext(invocation, invocation.getActualParameterTypes()));
//      }
//    }
//
//    // 4) Prefix
//    InvocationTarget target = beta.getTarget();
//    alphaExprIterator = alphaExprs.iterator();
//    if (target != null) {
//      while (alphaExprIterator.hasNext()) {
//        InvocationTarget prefix = target.clone();
//        // prefixes are evaluated in their original context: contextBeta
//        prefix.setSpecificContext(contextBeta);
//        ((Expression)alphaExprIterator.next()).prefix(prefix);
//      }
//    }
//
//    // 5) Subtitute parameters
//    List params = beta.getMethod().getParameters();
//    List actualParams = beta.getActualParameters();
//    Iterator paramIter = params.iterator();
//    Iterator actualParamIter = actualParams.iterator();
//    while (paramIter.hasNext()) {
//      FormalParameter param = (FormalParameter)paramIter.next();
//      Expression actualParam = (Expression)actualParamIter.next();
//      Expression clonedParam = actualParam.clone();
//      // parameters are evaluated in the original context: contextBeta
//      clonedParam.setSpecificContext(contextBeta);
//      alphaExprIterator = alphaExprs.iterator();
//      while (alphaExprIterator.hasNext()) {
//        ((Expression)alphaExprIterator.next()).substituteParameter(param.getName(), clonedParam);
//      }
//    }
//    // 6) Set new context
//    stub.setContext(new AnchorExpandedContext(contextAlpha, contextBeta, stub));
//    // invocations (method and variable) on 'this' need to get a special context.
//    return clonedAlpha;
//  }
//
//  /**
//   * Check whether the given invocation is directly compatible with an invocation of the other exception clause.
//   *
//   * @param inv
//   * @param other
//   * @param propagatedTypes
//   * @return
//   * @throws MetamodelException
//   */
//  public boolean isDirectlyCompatibleWith(final Invocation inv, final ExceptionClause other, final FilterClause filterClause) throws MetamodelException {
//    try {
//      return new PrimitivePredicate() {
//        public boolean eval(Object o2) throws MetamodelException {
//          if(! (o2 instanceof AnchoredExceptionDeclaration)) {
//            return false;
//          }
//          AnchoredExceptionDeclaration ad = (AnchoredExceptionDeclaration)o2;
//          Invocation otherInv = ad.getInvocation();
//          boolean otherInvCompat = otherInv.compatibleWith(inv);
//          boolean adNull = ad.getFilterClause() == null;
//
//          boolean adSuper = false;
//          if(! adNull) {
//          	adSuper = ad.getFilterClause().strongerThan(filterClause);
//          }
//          boolean result = otherInvCompat &&
//                (
//                  (adNull ) ||
//                  (adSuper)
//                );
//          return result;
//        }
//      }.exists(other.getDeclarations());
//    }
//    catch (MetamodelException e) {
//      throw e;
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      throw new Error();
//    }
//  }
//
//  public AnchoredExceptionDeclaration clone() {
//    FilterClause clause = null;
//    if(getFilterClause() != null) {
//      clause = getFilterClause().clone();
//    }
//    return new AnchoredExceptionDeclaration((NonConstructorInvocation)getInvocation().clone(), clause);
//  }
//
//  public Set getRawExceptionTypes() throws MetamodelException {
//    return getInvocation().getMethodExceptions();
//  }
//
//  public Set getExceptionTypes(Invocation invocation) throws MetamodelException {
//    Invocation nc = getClonedInvocation(invocation, getInvocation());
//    Set result = nc.getMethodExceptions();
//    if(getFilterClause() != null) {
//      result = getFilterClause().filteredTypes(result);
//    }
//    return result;
//  }
//
//  public Set getWorstCaseExceptionTypes() throws MetamodelException {
//    Set result = getInvocation().getMethodExceptions();
//    if(getFilterClause() != null) {
//      result = getFilterClause().filteredTypes(result);
//    }
//    return result;
//  }
//
//	/*****************
//   * FILTER CLAUSE *
//   *****************/
//
//  private Reference<AnchoredExceptionDeclaration,FilterClause> _filterClause = new Reference<AnchoredExceptionDeclaration,FilterClause>(this);
//
//  public FilterClause getFilterClause() {
//    return _filterClause.getOtherEnd();
//  }
//
//  public void setFilterClause(FilterClause clause) {
//    if(clause == null) {
//      _filterClause.connectTo(null);
//    }
//    else {
//      _filterClause.connectTo(clause.getParentLink());
//    }
//  }
//
//  public Reference<AnchoredExceptionDeclaration,FilterClause> getFilterClauseLink() {
//  	return _filterClause;
//  }
//
//
//  public boolean hasValidAccessibility() throws MetamodelException {
//    try {
//      return getInvocation().getAccessibilityDomain().atLeastAsAccessibleAs(((Method)((ExceptionClause)getParent()).getParent()).getAccessibilityDomain());
//    }
//    catch(ClassCastException exc) {
//      throw new Error("Invoking validVisibility on a cloned exception declaration.");
//    }
//  }
//
// /*@
//   @ also public behavior
//   @
//   @ post \result.contains(getInvocation());
//   @ post getPropagationClause() != null ==> \result.contains(getPropagationClause());
//   @*/
//  public List getChildren() {
//    List result = Util.createNonNullList(getFilterClause());
//    result.add(getInvocation());
//    return result;
//  }
//
//
//}
//
////class AnchorExpandedContext extends Context {
////
////  public AnchorExpandedContext(Context contextAlpha, Context contextBeta, NamespacePartElement parent) {
////    _contextBeta = contextBeta;
////    _contextAlpha = contextAlpha;
////    _parent = parent;
////  }
////
////  public Context getOriginalContext() {
////    return _contextAlpha;
////  }
////
////  protected NamespacePartElement _parent;
////
////  public NamespacePartElement getParent() {
////    return _parent;
////  }
////
////  protected Context _contextBeta;
////
////  protected Context _contextAlpha;
////
////  public Context cloneContext() {
////    return new AnchorExpandedContext(_contextAlpha.cloneContext(), _contextBeta.cloneContext(), _parent);
////  }
////
////  public Type findType(String name) throws MetamodelException {
////    return getOriginalContext().findType(name);
////  }
////
//////  public VariableOrType findVariableOrType(String name) throws NotResolvedException {
//////    return getOriginalContext().findVariableOrType(name);
//////  }
////
////  public Variable findVariable(String name) throws MetamodelException {
////    return getOriginalContext().findVariable(name);
////  }
////
//////  public Method findMethod(String name, List paramTypes, Class type) throws NotResolvedException {
//////    return getParentContext().findMethod(name, paramTypes, type);
//////  }
////
////
////  public Context getParentContext() {
////    return _contextBeta;
////  }
////
////  public Target findElement(String name) throws MetamodelException {
////    return getOriginalContext().findElement(name);
////  }
////
////  public NamespaceOrType findNamespaceOrType(String name) throws MetamodelException {
////    return getOriginalContext().findNamespaceOrType(name);
////  }
////
////  public RegularMethod findMethod(String name, List paramTypes) throws MetamodelException {
////    return getParentContext().findMethod(name, paramTypes);
////  }
////
////  public PrefixOperator findPrefixOperator(String name, List paramTypes) throws MetamodelException {
////    return getParentContext().findPrefixOperator(name, paramTypes);
////  }
////
////  public InfixOperator findInfixOperator(String name, List paramTypes) throws MetamodelException {
////    return getParentContext().findInfixOperator(name, paramTypes);
////  }
////
////  public PostfixOperator findPostfixOperator(String name, List paramTypes) throws MetamodelException {
////    return getParentContext().findPostfixOperator(name, paramTypes);
////  }
////
////}
////
////class SignatureBindingContext extends Context {
////
////  public SignatureBindingContext(Invocation invocation, List argumentTypes) {
////    _argTypes = argumentTypes;
////    _invocation = invocation;
////  }
////
////  private Invocation _invocation;
////
////  private List _argTypes;
////
////  public Type findType(String name) throws MetamodelException {
////    return getOriginalContext().findType(name);
////  }
////
////  public Context getParentContext() throws MetamodelException {
////    return ((ExpressionContainer)_invocation.getParent()).getContext(getParent());
////  }
////
////  public Context getOriginalContext() throws MetamodelException {
////    return getParentContext();
////  }
////
////  public Variable findVariable(String name) throws MetamodelException {
////    return getOriginalContext().findVariable(name);
////  }
////
////  public Context cloneContext() {
////    return new SignatureBindingContext(_invocation, new ArrayList(_argTypes));
////  }
////
////  public RegularMethod findMethod(RegularMethodInvocation invocation) throws MetamodelException {
////    if(invocation == getParent()) {
////      if(invocation.getTarget() != null) {
////        return invocation.getTarget().getTargetContext().findMethod(invocation.getName(), _argTypes);
////      }
////      else {
////        return findMethod(invocation.getName(), _argTypes);
////      }
////    }
////    else {
////      return super.findMethod(invocation);
////    }
////  }
////
////  public PrefixOperator findPrefixOperator(PrefixOperatorInvocation invocation) throws MetamodelException {
////    if(invocation == getParent()) {
////      if(invocation.getTarget() != null) {
////        return invocation.getTarget().getTargetContext().findPrefixOperator(invocation.getName(), _argTypes);
////      }
////      else {
////        return findPrefixOperator(invocation.getName(), _argTypes);
////      }
////    }
////    else {
////      return super.findPrefixOperator(invocation);
////    }
////  }
////
////  public PostfixOperator findPostfixOperator(PostfixOperatorInvocation invocation) throws MetamodelException {
////    if(invocation == getParent()) {
////      if(invocation.getTarget() != null) {
////        return invocation.getTarget().getTargetContext().findPostfixOperator(invocation.getName(), _argTypes);
////      }
////      else {
////        return findPostfixOperator(invocation.getName(), _argTypes);
////      }
////    }
////    else {
////      return super.findPostfixOperator(invocation);
////    }
////  }
////
////
////  public InfixOperator findInfixOperator(InfixOperatorInvocation invocation) throws MetamodelException {
////    if(invocation == getParent()) {
////      if(invocation.getTarget() != null) {
////        return invocation.getTarget().getTargetContext().findInfixOperator(invocation.getName(), _argTypes);
////      }
////      else {
////        return findInfixOperator(invocation.getName(), _argTypes);
////      }
////    }
////    else {
////      return super.findInfixOperator(invocation);
////    }
////  }
////
////  public NamespacePartElement getParent() {
////    return _invocation;
////  }
////
////  public Target findElement(String name) throws MetamodelException {
////    return getOriginalContext().findElement(name);
////  }
////
////  public NamespaceOrType findNamespaceOrType(String name) throws MetamodelException {
////    return getOriginalContext().findNamespaceOrType(name);
////  }
////
////  public RegularMethod findMethod(String name, List paramTypes) throws MetamodelException {
////    return getOriginalContext().findMethod(name, paramTypes);
////  }
////
////  public PrefixOperator findPrefixOperator(String name, List paramTypes) throws MetamodelException {
////    return getOriginalContext().findPrefixOperator(name, paramTypes);
////  }
////
////  public InfixOperator findInfixOperator(String name, List paramTypes) throws MetamodelException {
////    return getOriginalContext().findInfixOperator(name, paramTypes);
////  }
////
////  public PostfixOperator findPostfixOperator(String name, List paramTypes) throws MetamodelException {
////    return getOriginalContext().findPostfixOperator(name, paramTypes);
////  }
////
////}
