package chameleon.core.expression;

import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.context.LexicalContext;
import chameleon.core.type.StubTypeElement;
import chameleon.core.type.TypeDescendant;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class StubExpressionContainer extends StubTypeElement<StubExpressionContainer> implements ExpressionContainer<StubExpressionContainer,TypeDescendant> {
  
  public StubExpressionContainer(ExpressionContainer parent, Expression expr) {
    super(parent); //FIXME this entire class should be removed I think, and use a adaptable context graph for anchored exception declarations
    _expr.connectTo(expr.getParentLink());
  }

 	private Reference _expr = new Reference(this);

  
  public Expression getExpression() {
    return (Expression)_expr.getOtherEnd();
  }
  
 /*@
   @ also public behavior
   @
   @ post getExpression() != null ==> \result.contains(getExpression());
   @ post \result.size() == 1;
   @*/
  public List getChildren() {
    return Util.createNonNullList(getExpression());
  }

  @Override
  public StubExpressionContainer clone() {
      return new StubExpressionContainer(null,getExpression().clone());
  }

}
