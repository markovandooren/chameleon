package chameleon.core.method.exception;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.type.StubTypeElement;
import chameleon.util.Util;

/**
 * @author marko
 */
public class StubExceptionClauseContainer extends StubTypeElement<StubExceptionClauseContainer> {
  
    public StubExceptionClauseContainer(Element parent, ExceptionClause exc) {
      super(parent); //FIXME this entire class should be removed, and replaced by a more flexible lookup mechanism so we won't have to duplicate items.
      _exceptionClause.connectTo(exc.parentLink());
    }

	/**
	 * 
	 * @uml.property name="_exceptionClause"
	 * @uml.associationEnd 
	 * @uml.property name="_exceptionClause" multiplicity="(0 -1)" elementType="chameleon.core.method.exception.ExceptionClause"
	 */
	private SingleAssociation _exceptionClause = new SingleAssociation(this);

    
    public ExceptionClause getExceptionClause() {
      return (ExceptionClause)_exceptionClause.getOtherEnd();
    }

   /*@
     @ also public behavior
     @
     @ post \result.contains(getExceptionClause());
     @ post \result.size() == 1;
     @*/
    public List children() {
      return Util.createNonNullList(getExceptionClause());
    }

    @Override
    public StubExceptionClauseContainer clone() {
      StubExceptionClauseContainer result;
        result = new StubExceptionClauseContainer(null,getExceptionClause().clone());
      return result;
    }

}
