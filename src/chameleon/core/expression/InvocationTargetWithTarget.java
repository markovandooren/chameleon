package chameleon.core.expression;

import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.element.Element;
import chameleon.core.type.Type;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public abstract class InvocationTargetWithTarget<E extends InvocationTargetWithTarget> extends InvocationTarget<E,Element> implements ExpressionWithTarget<E, Element> {
  
  public InvocationTargetWithTarget() {
  }
  
  public InvocationTargetWithTarget(InvocationTarget target) {
  	setTarget(target);
  }
  
	/**
	 * TARGET
	 */
	private Reference<InvocationTarget,InvocationTarget> _target = new Reference<InvocationTarget,InvocationTarget>(this);

  public InvocationTarget<?,?> getTarget() {
    return _target.getOtherEnd();
  }

  public void setTarget(InvocationTarget target) {
    if (target != null) {
      _target.connectTo(target.parentLink());
    }
    else {
      _target.connectTo(null);
    }
  }

  public List<Element> children() {
  	return Util.createNonNullList(getTarget());
  }
}