package org.aikodi.chameleon.oo.method;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.statement.Block;
import org.aikodi.chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public class RegularImplementation extends Implementation  {

  public RegularImplementation(Block body) {
	  setBody(body);
  }

	/**
	 * BODY
	 */
	private Single<Block> _body = new Single<Block>(this, "body");

  public void setBody(Block block) {
    set(_body,block);
  }

  @Override
public Block getBody() {
    return _body.getOtherEnd();
  }

  @Override
protected RegularImplementation cloneSelf() {
    return new RegularImplementation(null);
  }

  @Override
public boolean compatible() throws LookupException {
  	throw new Error("Implement exception anchors again.");
//    if(getBody() == null) {
//      return true;
//    }
//    Collection declarations = getBody().getCEL().getDeclarations();
//    final ExceptionClause exceptionClause = new ExceptionClause();
//    new Visitor() {
//      public void visit(Object element) {
//        exceptionClause.add((ExceptionDeclaration)element);
//      }
//    }.applyTo(declarations);
//    StubExceptionClauseContainer stub = new StubExceptionClauseContainer(getParent(),exceptionClause,getParent().lexicalContext());
//    return exceptionClause.compatibleWith(getParent().getExceptionClause());
  }
  
 /*@
   @ public behavior
   @
   @ post \result == (\forall TryStatement ts; getAllStatements().contains(ts);
   @                    ts.hasValidCatchClauses()); 
   @*/
  @Override
public boolean hasValidCatchClauses() throws LookupException {
  	//@FIXME reimplement this
//    try {
//      Collection<TryStatement> statements = getBody().getDescendants(TryStatement.class);
//      return new PrimitivePredicate<TryStatement>() {
//        public boolean eval(TryStatement tryStatement) throws MetamodelException {
//          return tryStatement.hasValidCatchClauses();
//        }
//      }.forAll(statements);
//    }
//    catch (MetamodelException e) {
//      throw e;
//    }
//    catch (Exception e) {
//      e.printStackTrace();
      throw new Error();
//    }
  }

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	@Override
	public boolean complete() {
		return getBody() != null;
	}

}
