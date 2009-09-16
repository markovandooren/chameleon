package chameleon.core.expression;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.type.StubTypeElement;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class StubExpressionContainer extends StubTypeElement<StubExpressionContainer> {
  
  public StubExpressionContainer(Element parent, Expression expr) {
    super(parent); //FIXME this entire class should be removed I think, and use a adaptable context graph for anchored exception declarations
    _expr.connectTo(expr.parentLink());
  }

 	private SingleAssociation _expr = new SingleAssociation(this);

  
  public Expression getExpression() {
    return (Expression)_expr.getOtherEnd();
  }
  
 /*@
   @ also public behavior
   @
   @ post getExpression() != null ==> \result.contains(getExpression());
   @ post \result.size() == 1;
   @*/
  public List children() {
    return Util.createNonNullList(getExpression());
  }

  @Override
  public StubExpressionContainer clone() {
      return new StubExpressionContainer(null,getExpression().clone());
  }

}
