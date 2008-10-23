package chameleon.core.variable;

import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.expression.Expression;
import chameleon.core.expression.ExpressionContainer;
import chameleon.core.statement.CheckedExceptionList;
import chameleon.core.statement.ExceptionSource;
import chameleon.core.type.TypeReference;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public abstract class InitializableVariable<E extends InitializableVariable, P extends VariableContainer>
                extends Variable<E,P> implements ExpressionContainer<E,P>, ExceptionSource<E,P> {

  public InitializableVariable(VariableSignature sig, TypeReference type, Expression initCode) {
    super(sig, type);
    setInitialization(initCode);
  }

	/**
	 * INITIALIZATION EXPRESSION 
	 */
  
	private Reference<InitializableVariable,Expression> _init = new Reference<InitializableVariable,Expression>(this);

	public Expression getInitialization() {
    return _init.getOtherEnd();
  }
  
  public void setInitialization(Expression expr) {
    if(expr != null) {
      _init.connectTo(expr.getParentLink());
    }
    else {
      _init.connectTo(null);
    }
  }
  
  public CheckedExceptionList getCEL() throws MetamodelException {
    if(getInitialization() != null) {
      return getInitialization().getCEL();
    }
    else {
      return new CheckedExceptionList(getNamespace().language());
    }
  }

  public CheckedExceptionList getAbsCEL() throws MetamodelException {
    if(getInitialization() != null) {
      return getInitialization().getAbsCEL();
    }
    else {
      return new CheckedExceptionList(getNamespace().language());
    }
  }

  public List getChildren() {
    List result = super.getChildren();
    Util.addNonNull(getInitialization(), result);
    return result;
  }
}
